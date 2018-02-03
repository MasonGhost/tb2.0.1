package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal

import com.zhiyicx.common.base.BaseJsonV2
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.base.AppApplication
import com.zhiyicx.thinksnsplus.base.AppBasePresenter
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository

import javax.inject.Inject

import rx.Subscription
import rx.functions.Action0

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
class IntegrationWithdrawalsPresenter @Inject
constructor(rootView: IntegrationWithdrawalsContract.View) : AppBasePresenter<IntegrationWithdrawalsContract.View>(rootView), IntegrationWithdrawalsContract.Presenter {
    @Inject
    lateinit var mBillRepository: BillRepository

    override fun integrationWithdrawals(amount: Int) {

        val subscribe = mBillRepository.integrationWithdrawals(amount)
                .doAfterTerminate { mRootView.setSureBtEnable(true) }
                .subscribe(object : BaseSubscribeForV2<BaseJsonV2<*>>() {
                    override fun onSuccess(data: BaseJsonV2<*>) {
                        try {
                            val userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault())
                            userInfoBean.currency.sum = userInfoBean.currency.sum - amount
                            mUserInfoBeanGreenDao.insertOrReplace(userInfoBean)
                        } catch (ignored: Exception) {
                        }

                        mRootView.showSnackSuccessMessage(mContext.resources.getString(R.string.integration_withdrawals_report_success))
                    }

                    override fun onFailure(message: String, code: Int) {
                        super.onFailure(message, code)
                        mRootView.showSnackErrorMessage(message)

                    }

                    override fun onException(throwable: Throwable) {
                        super.onException(throwable)
                        mRootView.showSnackSuccessMessage(mContext.resources.getString(R.string.err_net_not_work))
                    }
                })
        addSubscrebe(subscribe)

    }
}
