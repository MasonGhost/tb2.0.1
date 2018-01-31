package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail

import com.zhiyicx.baseproject.base.TSListFragment
import com.zhiyicx.thinksnsplus.base.AppBasePresenter
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository
import java.util.*
import javax.inject.Inject

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/1/31
 * @Contact master.jungle68@gmail.com
 */
class IntegrationDetailPresenter @Inject
constructor(rootView: IntegrationDetailContract.View) : AppBasePresenter<IntegrationDetailContract.View>(rootView), IntegrationDetailContract.Presenter {


    @Inject
    lateinit var mBillRepository: BillRepository

    override fun requestNetData(maxId: Long?, isLoadMore: Boolean) {
        val subscribe = mBillRepository.integrationOrdersSuccess(TSListFragment.DEFAULT_PAGE_SIZE, maxId!!.toInt(), mRootView.mChooseType,
                mRootView
                        .billType)
                .subscribe(object : BaseSubscribeForV2<List<RechargeSuccessV2Bean>>() {
                    override fun onSuccess(data: List<RechargeSuccessV2Bean>) {
                        mRootView.onNetResponseSuccess(data, isLoadMore)
                    }

                    override fun onFailure(message: String, code: Int) {
                        super.onFailure(message, code)
                        mRootView.showMessage(message)
                    }

                    override fun onException(throwable: Throwable) {
                        super.onException(throwable)
                        mRootView.onResponseError(throwable, isLoadMore)
                    }
                })
        addSubscrebe(subscribe)
    }

    override fun requestCacheData(maxId: Long?, isLoadMore: Boolean) {
        mRootView.onCacheResponseSuccess(ArrayList(), isLoadMore)
    }

    override fun insertOrUpdateData(data: List<RechargeSuccessV2Bean>, isLoadMore: Boolean): Boolean {
        return true
    }

}
