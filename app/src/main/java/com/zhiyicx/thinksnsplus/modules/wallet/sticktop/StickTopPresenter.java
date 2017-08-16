package com.zhiyicx.thinksnsplus.modules.wallet.sticktop;


import android.os.Bundle;

import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;
import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_DYNAMIC;
import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_INFO;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class StickTopPresenter extends AppBasePresenter<StickTopContract.Repository, StickTopContract.View>
        implements StickTopContract.Presenter {

    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Inject
    public StickTopPresenter(StickTopContract.Repository repository, StickTopContract.View rootView) {
        super(repository, rootView);
    }

    /**
     * 内容置顶
     *
     * @param parent_id
     */
    @Override
    public void stickTop(long parent_id) {
        if (mRootView.getInputMoney() != (int) mRootView.getInputMoney()) {
            mRootView.initStickTopInstructionsPop();
            return;
        }
        if (mRootView.insufficientBalance()) {
            mRootView.gotoRecharge();
            return;
        }
        if (parent_id < 0) {
            return;
        }
        Subscription subscription = mRepository.stickTop(mRootView.getType(), parent_id, PayConfig.realCurrencyYuan2Fen(mRootView.getInputMoney() * mRootView.getTopDyas()), mRootView.getTopDyas())
                .doOnSubscribe(() ->
                        mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing))
                )
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Integer>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Integer> data) {
                        switch (mRootView.getType()) {
                            case TYPE_DYNAMIC:
                                mRootView.showSnackSuccessMessage(mContext.getString(R.string.dynamic_list_top_dynamic_success));
                                break;
                            case TYPE_INFO:
                                mRootView.showSnackSuccessMessage(mContext.getString(R.string.dynamic_list_top_info_success));
                                break;
                            default:
                                mRootView.showSnackSuccessMessage(mContext.getString(R.string.dynamic_list_top_success));

                        }

                        mRootView.topSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });

        addSubscrebe(subscription);
    }

    /**
     * 内容所属的评论置顶
     *
     * @param parent_id
     * @param child_id
     */
    @Override
    public void stickTop(long parent_id, long child_id) {
        if (mRootView.getInputMoney() != (int) mRootView.getInputMoney()) {
            mRootView.initStickTopInstructionsPop();
            return;
        }
        if (mRootView.insufficientBalance()) {
            mRootView.gotoRecharge();
            return;
        }
        if (parent_id < 0) {
            return;
        }
        Subscription subscription = mRepository.stickTop(mRootView.getType(), parent_id, child_id, PayConfig.realCurrencyYuan2Fen(mRootView.getInputMoney() * mRootView.getTopDyas()), mRootView.getTopDyas())
                .doOnSubscribe(() ->
                        mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing))
                )
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Integer>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Integer> data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.comment_top_success));
                        mRootView.topSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });

        addSubscrebe(subscription);
    }

    @Override
    public double getBalance() {

        Subscription userInfoSub = mUserInfoRepository.getCurrentLoginUserInfo()
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        if (data.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(data.getWallet());
                        }
                        int ratio = mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
                        mRootView.updateBalance(data.getWallet() != null ? PayConfig.realCurrencyFen2Yuan(data.getWallet().getBalance()) : 0);
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
        addSubscrebe(userInfoSub);

        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (authBean != null) {
            WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(authBean.getUser_id());
            if (walletBean == null) {
                return 0;
            }
            int ratio = mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
            return PayConfig.realCurrencyFen2Yuan(walletBean.getBalance());
        }
        return 0;
    }
}
