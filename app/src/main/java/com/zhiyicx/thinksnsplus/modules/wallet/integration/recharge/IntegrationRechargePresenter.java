package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationRechargePresenter extends AppBasePresenter<IntegrationRechargeContract.View> implements IntegrationRechargeContract
        .Presenter {


    @Inject
    public IntegrationRechargePresenter(IntegrationRechargeContract.View rootView) {
        super(rootView);
    }


    @Inject
    BillRepository mBillRepository;

    @Override
    public void getPayStr(String channel, double amount) {
        if (mRootView.getMoney() != (int) mRootView.getMoney() && mRootView.useInputMonye()) {
            mRootView.initmRechargeInstructionsPop();
            return;
        }
        mBillRepository.getIntegrationPayStr(channel, (long) amount,null).doOnSubscribe(() -> {
            mRootView.configSureBtn(false);
            mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing));
        }).subscribe(new BaseSubscribeForV2<PayStrV2Bean>() {
            @Override
            protected void onSuccess(PayStrV2Bean data) {
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
        Subscription subscribe = mBillRepository.integrationRechargeSuccess(charge).subscribe(new BaseSubscribeForV2<RechargeSuccessV2Bean>() {
            @Override
            protected void onSuccess(RechargeSuccessV2Bean data) {
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
        addSubscrebe(subscribe);
    }

    @Override
    public void rechargeSuccessCallBack(String charge) {
//        不需要获取详细信息
//        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
//        backgroundRequestTaskBean.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
//        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.GET);
//        backgroundRequestTaskBean.setPath(ApiConfig.APP_DOMAIN + String.format(ApiConfig.APP_PAHT_WALLET_RECHARGE_SUCCESS_CALLBACK_FORMAT, charge));
//        mBackgroundRequestTaskBeanGreenDao.insertOrReplace(backgroundRequestTaskBean);
//        Subscription subscribe = mBillRepository.rechargeSuccessCallBack(charge).subscribe(new BaseSubscribeForV2<RechargeSuccessBean>() {
//            @Override
//            protected void onSuccess(RechargeSuccessBean data) {
//                mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
//                mRootView.rechargeSuccess(data);
//            }
//
//            @Override
//            protected void onFailure(String message, int code) {
//                super.onFailure(message, code);
//            }
//
//            @Override
//            protected void onException(Throwable throwable) {
//                super.onException(throwable);
//            }
//        });
//        addSubscrebe(subscribe);
    }

}
