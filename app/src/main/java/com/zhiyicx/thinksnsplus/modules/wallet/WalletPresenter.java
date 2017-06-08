package com.zhiyicx.thinksnsplus.modules.wallet;

import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class WalletPresenter extends AppBasePresenter<WalletContract.Repository, WalletContract.View> implements WalletContract.Presenter {
    public static final int DEFAULT_LOADING_SHOW_TIME = 2;

    /**
     * action tag
     */
    public static final int TAG_DEfault = 0; // do nothing
    public static final int TAG_RECHARGE = 1; // recharge
    public static final int TAG_WITHDRAW = 2; // withdraw

    @Inject
    AuthRepository mIAuthRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    private boolean mIsUsreInfoRequseted = false;// 用户信息是否拿到了

    WalletConfigBean mWalletConfigBean; // 钱包配置信息，必须的数据


    @Inject
    public WalletPresenter(WalletContract.Repository repository, WalletContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void updateUserInfo() {
        getWalletConfigFromServer(TAG_DEfault, false); // 默认主动获取一次
        Subscription timerSub = Observable.timer(DEFAULT_LOADING_SHOW_TIME, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        if (!mIsUsreInfoRequseted) {
                            mRootView.handleLoading(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }
                });
        Subscription userInfoSub = mUserInfoRepository.getCurrentLoginUserInfo()
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.handleLoading(false);
                        mIsUsreInfoRequseted = true;
                    }
                })
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {

                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        if (data.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(data.getWallet());
                        }
                        mRootView.updateBalance(data.getWallet() != null ? data.getWallet().getBalance() : 0);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackWarningMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(timerSub);
        addSubscrebe(userInfoSub);
    }

    /**
     * @return check first look  need show  tip pop
     */
    @Override
    public boolean checkIsNeedTipPop() {
        boolean isNotFrist = SharePreferenceUtils.getBoolean(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_LOOK_WALLET);
        if (!isNotFrist) {
            SharePreferenceUtils.saveBoolean(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_LOOK_WALLET, true);
        }
        return !isNotFrist;
    }

    /**
     * check wallet config info, if walletconfig has cach used it or get it from server
     *
     * @param tag action tag
     */
    @Override
    public void checkWalletConfig(int tag) {
        if (mWalletConfigBean != null) {
            mRootView.walletConfigCallBack(mWalletConfigBean, tag);
            return;
        }
        getWalletConfigFromServer(tag, true);

    }


    /**
     * get wallet config info from server
     *
     * @param tag       action tag, 1 recharge 2 withdraw
     * @param isNeedTip true show tip
     */
    private void getWalletConfigFromServer(final int tag, final boolean isNeedTip) {

        final Subscription walletConfigSub = mRepository.getWalletConfig()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (isNeedTip) {
                            mRootView.showSnackLoadingMessage(mContext.getString(R.string.wallet_config_info_get_loading_tip));
                        }
                    }
                })
                .subscribe(new BaseSubscribeForV2<WalletConfigBean>() {
                    @Override
                    protected void onSuccess(WalletConfigBean data) {
                        mWalletConfigBean = data;
                        if (isNeedTip) {
                            mRootView.showSnackSuccessMessage(mContext.getString(R.string.get_success));
                        }
                        mRootView.walletConfigCallBack(data, tag);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        if (isNeedTip) {
                            mRootView.showSnackErrorMessage(message);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isNeedTip) {
                            mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                        }
                    }
                });
        addSubscrebe(walletConfigSub);
    }


}
