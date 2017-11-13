package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.local.BackgroundRequestTaskBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class RechargePresenter extends AppBasePresenter<RechargeContract.Repository, RechargeContract.View> implements RechargeContract.Presenter {

    @Inject
    AuthRepository mIAuthRepository;

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    BackgroundRequestTaskBeanGreenDaoImpl mBackgroundRequestTaskBeanGreenDao;

    @Inject
    public RechargePresenter(RechargeContract.Repository repository, RechargeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void getPayStr(String channel, double amount) {
        if (mRootView.getMoney() != (int) mRootView.getMoney() && mRootView.useInputMonye()) {
            mRootView.initmRechargeInstructionsPop();
            return;
        }
        mSystemRepository.getPayStr(channel, (long) amount).doOnSubscribe(() -> {
            mRootView.configSureBtn(false);
            mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing));
        }).subscribe(new BaseSubscribeForV2<PayStrBean>() {
            @Override
            protected void onSuccess(PayStrBean data) {
                try {
                    mRootView.showSnackSuccessMessage(mContext.getString(R.string.recharge_credentials_succes));
                } catch (Exception e) {
                }

                mRootView.payCredentialsResult(data);
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
    }

    @Override
    public void rechargeSuccess(String charge) {
        mRepository.rechargeSuccess(charge).subscribe(new BaseSubscribeForV2<RechargeSuccessBean>() {
            @Override
            protected void onSuccess(RechargeSuccessBean data) {
                rechargeSuccessCallBack(data.getId() + "");
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
            }
        });
    }

    @Override
    public void rechargeSuccessCallBack(String charge) {
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.GET);
        backgroundRequestTaskBean.setPath(ApiConfig.APP_DOMAIN + String.format(ApiConfig.APP_PAHT_WALLET_RECHARGE_SUCCESS_CALLBACK_FORMAT, charge));
        mBackgroundRequestTaskBeanGreenDao.insertOrReplace(backgroundRequestTaskBean);
        mRepository.rechargeSuccessCallBack(charge).subscribe(new BaseSubscribeForV2<RechargeSuccessBean>() {
            @Override
            protected void onSuccess(RechargeSuccessBean data) {
                mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                try {
                    mRootView.rechargeSuccess(data);
                } catch (Exception e) {
                }
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
            }
        });
    }
}
