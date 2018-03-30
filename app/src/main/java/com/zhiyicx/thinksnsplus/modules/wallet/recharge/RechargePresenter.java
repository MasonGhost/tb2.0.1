package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.BuildConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.local.BackgroundRequestTaskBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class RechargePresenter extends AppBasePresenter<RechargeContract.View> implements RechargeContract.Presenter {


    @Inject
    BackgroundRequestTaskBeanGreenDaoImpl mBackgroundRequestTaskBeanGreenDao;

    @Inject
    BillRepository mBillRepository;

    @Inject
    public RechargePresenter(RechargeContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getPayStr(String channel, double amount) {
        if (mRootView.getMoney() != (int) mRootView.getMoney() && mRootView.useInputMonye()) {
            mRootView.initmRechargeInstructionsPop();
            return;
        }
        mBillRepository.getPayStr(channel, amount)
                .doOnSubscribe(() -> {
                    mRootView.configSureBtn(false);
                    mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing));
                }).subscribe(new BaseSubscribeForV2<PayStrV2Bean>() {
            @Override
            protected void onSuccess(PayStrV2Bean data) {
                mRootView.payCredentialsResult(data);
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
                mRootView.showSnackErrorMessage(message);
            }

            @Override
            protected void onException(Throwable throwable) {
                showErrorTip(throwable);
            }

        });
    }

    @Override
    public void rechargeSuccess(String charge) {
        Subscription subscribe = mBillRepository.rechargeSuccess(charge).subscribe(new BaseSubscribeForV2<RechargeSuccessBean>() {
            @Override
            protected void onSuccess(RechargeSuccessBean data) {
                mRootView.showSnackSuccessMessage(mContext.getString(R.string.recharge_success));

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
        addSubscrebe(subscribe);
    }

    @Override
    public void rechargeSuccessCallBack(String charge) {

    }
}
