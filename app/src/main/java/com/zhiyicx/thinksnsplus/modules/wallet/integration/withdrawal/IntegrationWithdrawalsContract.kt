package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal

import com.zhiyicx.baseproject.base.IBaseTouristPresenter
import com.zhiyicx.common.mvp.i.IBaseView
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean
import com.zhiyicx.tspay.TSPayClient

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

interface IntegrationWithdrawalsContract {

    interface View : IBaseView<Presenter> {
        fun setSureBtEnable(enable: Boolean)
    }

    interface Presenter : IBaseTouristPresenter {
        fun integrationWithdrawals(amount: Int)
    }
}
