package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail

import com.zhiyicx.baseproject.base.ITSListPresenter
import com.zhiyicx.baseproject.base.ITSListView
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/1/31
 * @Contact master.jungle68@gmail.com
 */
interface IntegrationDetailContract {
    interface View : ITSListView<RechargeSuccessV2Bean, Presenter> {
        val tsAdapter: HeaderAndFooterWrapper<*>

        val billType: Int?

        val mChooseType: String?
    }

    interface Presenter : ITSListPresenter<RechargeSuccessV2Bean>

}
