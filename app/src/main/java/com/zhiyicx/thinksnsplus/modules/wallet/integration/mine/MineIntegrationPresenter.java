package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine;

import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class MineIntegrationPresenter extends AppBasePresenter<MineIntegrationContract.View> implements MineIntegrationContract.Presenter {
    public static final int DEFAULT_LOADING_SHOW_TIME = 1;


    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    BillRepository mBillRepository;


    /**
     * 用户信息是否拿到了
     */
    private boolean mIsUsreInfoRequseted = false;

    /**
     * 钱包配置信息，必须的数据
     */
    private IntegrationConfigBean mIntegrationConfigBean;


    @Inject
    public MineIntegrationPresenter(MineIntegrationContract.View rootView) {
        super(rootView);
    }

    @Override
    public void updateUserInfo() {
        Subscription timerSub = Observable.timer(DEFAULT_LOADING_SHOW_TIME, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (!mIsUsreInfoRequseted) {
                        mRootView.handleLoading(true);
                    }
                });

        Subscription userInfoSub = mUserInfoRepository.getLocalUserInfoBeforeNet(AppApplication.getMyUserIdWithdefault())
                .doAfterTerminate(() -> {
                    mRootView.handleLoading(false);
                    mIsUsreInfoRequseted = true;
                })
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        if (data.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(data.getWallet());
                        }
                        mRootView.updateBalance(data.getFormatCurrencyNum());
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
     * check wallet config info, if integrationconfig has cach used it or get it from server
     *
     * @param tag action tag
     */
    @Override
    public void checkIntegrationConfig(int tag, final boolean isNeedTip) {
        if (mIntegrationConfigBean != null) {
            mRootView.integrationConfigCallBack(mIntegrationConfigBean, tag);
            return;
        }
        getWalletConfigFromServer(tag, isNeedTip);

    }

    @Override
    public String getTipPopRule() {
        if (mIntegrationConfigBean == null) {
            return mContext.getResources().getString(R.string.integration_rule);
        }
        return mIntegrationConfigBean.getRule();

    }

    /**
     * get wallet config info from server
     *
     * @param tag       action tag, 1 recharge 2 withdraw
     * @param isNeedTip true show tip
     */
    private void getWalletConfigFromServer(final int tag, final boolean isNeedTip) {

        final Subscription walletConfigSub = mBillRepository.getIntegrationConfig()
                .doOnSubscribe(() -> {
                    if (isNeedTip) {
                        mRootView.showSnackLoadingMessage(mContext.getString(R.string.integration_config_info_get_loading_tip));
                    }
                })
                .subscribe(new BaseSubscribeForV2<IntegrationConfigBean>() {
                    @Override
                    protected void onSuccess(IntegrationConfigBean data) {
                        mIntegrationConfigBean = data;
                        if (isNeedTip) {
                            mRootView.dismissSnackBar();
                        }
                        mRootView.integrationConfigCallBack(data, tag);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        if (isNeedTip) {
                            mRootView.showSnackErrorMessage(message);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        if (isNeedTip) {
                            mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                        }
                    }
                });
        addSubscrebe(walletConfigSub);
    }


}
