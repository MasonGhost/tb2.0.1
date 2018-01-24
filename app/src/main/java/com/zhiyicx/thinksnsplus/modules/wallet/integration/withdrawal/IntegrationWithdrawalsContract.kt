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

        val money: Double
        fun payCredentialsResult(payStrV2Bean: PayStrV2Bean)
        fun configSureBtn(enable: Boolean)
        fun rechargeSuccess(rechargeSuccessBean: RechargeSuccessBean)
        fun initmRechargeInstructionsPop()

        fun useInputMonye(): Boolean

    }

    interface Presenter : IBaseTouristPresenter {
        fun getPayStr(@TSPayClient.PayKey channel: String, amount: Double)
        fun rechargeSuccess(charge: String)
        fun rechargeSuccessCallBack(charge: String)
    }
}
