package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge;

import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action0;

import static com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_INTERVAL_TIME;
import static com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_MAX_COUNT;
import static com.zhiyicx.tspay.TSPayClient.CHANNEL_BALANCE;

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
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Override
    public void getPayStr(String channel, double amount) {
        if (mRootView.getMoney() != (int) mRootView.getMoney() && mRootView.useInputMonye()) {
            mRootView.initmRechargeInstructionsPop();
            return;
        }

        if (CHANNEL_BALANCE.equals(channel)) {
            mBillRepository.balance2Integration((long) amount)
                    .flatMap(baseJsonV2 -> mUserInfoRepository.getCurrentLoginUserInfo())
                    .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                        @Override
                        protected void onSuccess(UserInfoBean data) {
                            try {
                                mRootView.showSnackSuccessMessage(mContext.getString(R.string.handle_success));
                            } catch (Exception e) {
                            }

                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                            try {
                                mRootView.showSnackErrorMessage(message);
                            } catch (Exception igonred) {
                            }
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            try {
                                mRootView.showSnackErrorMessage(throwable.getMessage());
                            } catch (Exception igonred) {
                            }
                        }

                    });


        } else {

            mBillRepository.getIntegrationPayStr(channel, (long) amount, null).doOnSubscribe(() -> {
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
                    super.onException(throwable);
                    mRootView.showSnackErrorMessage(throwable.getMessage());
                }

            });
        }
    }

    @Override
    public void rechargeSuccess(String charge) {
        Subscription subscribe = mBillRepository.integrationRechargeSuccess(charge)
                .subscribe(new BaseSubscribeForV2<RechargeSuccessV2Bean>() {
                    @Override
                    protected void onSuccess(RechargeSuccessV2Bean data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.handle_success));
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
        mUserInfoRepository.getCurrentLoginUserInfo()
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                    }
                });
        mRootView.rechargeSuccess(null);
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

    /**
     * 更新积分配置
     */
    @Override
    public void getIntegrationConfigBean() {
        mRootView.handleLoading(true);
        Subscription subscribe = mBillRepository.getIntegrationConfig()
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .doAfterTerminate(() -> mRootView.handleLoading(false))
                .subscribe(new BaseSubscribeForV2<IntegrationConfigBean>() {
                    @Override
                    protected void onSuccess(IntegrationConfigBean data) {
                        mRootView.updateIntegrationConfig(true, data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.updateIntegrationConfig(false, null);

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.updateIntegrationConfig(false, null);

                    }
                });
        addSubscrebe(subscribe);

    }
}
