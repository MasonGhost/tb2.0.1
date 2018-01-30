package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge

import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.base.AppBasePresenter
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository

import javax.inject.Inject

import rx.Subscription

import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_INTERVAL_TIME
import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_MAX_COUNT
import com.zhiyicx.tspay.TSPayClient.CHANNEL_BALANCE

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
class IntegrationRechargePresenter @Inject
constructor(rootView: IntegrationRechargeContract.View) : AppBasePresenter<IntegrationRechargeContract.View>(rootView), IntegrationRechargeContract.Presenter {


    @Inject
    lateinit
    var mBillRepository: BillRepository
    @Inject
    lateinit
    var mUserInfoRepository: UserInfoRepository

    override fun getPayStr(channel: String, amount: Double) {
        if (mRootView.money != mRootView.money.toInt().toDouble() && mRootView.useInputMonye()) {
            mRootView.initmRechargeInstructionsPop()
            return
        }

        if (CHANNEL_BALANCE == channel) {
            mBillRepository!!.balance2Integration(amount.toLong())
                    .flatMap { baseJsonV2 -> mUserInfoRepository!!.currentLoginUserInfo }
                    .subscribe(object : BaseSubscribeForV2<UserInfoBean>() {
                        override fun onSuccess(data: UserInfoBean) {
                            try {
                                mRootView.showSnackSuccessMessage(mContext.getString(R.string.handle_success))
                            } catch (e: Exception) {
                            }

                        }

                        override fun onFailure(message: String, code: Int) {
                            super.onFailure(message, code)
                            try {
                                mRootView.showSnackErrorMessage(message)
                            } catch (igonred: Exception) {
                            }

                        }

                        override fun onException(throwable: Throwable) {
                            super.onException(throwable)
                            try {
                                mRootView.showSnackErrorMessage(throwable.message)
                            } catch (igonred: Exception) {
                            }

                        }

                    })


        } else {

            mBillRepository!!.getIntegrationPayStr(channel, amount.toLong(), null).doOnSubscribe {
                mRootView.configSureBtn(false)
                mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing))
            }.subscribe(object : BaseSubscribeForV2<PayStrV2Bean>() {
                override fun onSuccess(data: PayStrV2Bean) {
                    mRootView.payCredentialsResult(data)
                }

                override fun onFailure(message: String, code: Int) {
                    super.onFailure(message, code)
                    try {
                        mRootView.showSnackErrorMessage(message)
                    } catch (igonred: Exception) {
                    }

                }

                override fun onException(throwable: Throwable) {
                    super.onException(throwable)
                    try {
                        mRootView.showSnackErrorMessage(throwable.message)
                    } catch (igonred: Exception) {
                    }

                }

            })
        }
    }

    override fun rechargeSuccess(charge: String) {
        val subscribe = mBillRepository!!.integrationRechargeSuccess(charge)
                .subscribe(object : BaseSubscribeForV2<RechargeSuccessV2Bean>() {
                    override fun onSuccess(data: RechargeSuccessV2Bean) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.handle_success))
                        rechargeSuccessCallBack(data.id.toString() + "")
                    }

                    override fun onFailure(message: String, code: Int) {
                        super.onFailure(message, code)
                    }

                    override fun onException(throwable: Throwable) {
                        super.onException(throwable)
                    }
                })
        addSubscrebe(subscribe)
    }

    override fun rechargeSuccessCallBack(charge: String) {
        mUserInfoRepository!!.currentLoginUserInfo
                .subscribe(object : BaseSubscribeForV2<UserInfoBean>() {
                    override fun onSuccess(data: UserInfoBean) {}
                })
        mRootView.rechargeSuccess(null!!)
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
    override fun getIntegrationConfigBean() {
        mRootView.handleLoading(true)
        val subscribe = mBillRepository!!.integrationConfig
                .retryWhen(RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .doAfterTerminate { mRootView.handleLoading(false) }
                .subscribe(object : BaseSubscribeForV2<IntegrationConfigBean>() {
                    override fun onSuccess(data: IntegrationConfigBean) {
                        mRootView.updateIntegrationConfig(true, data)
                    }

                    override fun onFailure(message: String, code: Int) {
                        super.onFailure(message, code)
                        mRootView.updateIntegrationConfig(false, null!!)

                    }

                    override fun onException(throwable: Throwable) {
                        super.onException(throwable)
                        mRootView.updateIntegrationConfig(false, null!!)

                    }
                })
        addSubscrebe(subscribe)

    }
}
