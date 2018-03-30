package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge

import com.zhiyicx.baseproject.base.IBaseTouristPresenter
import com.zhiyicx.common.mvp.i.IBaseView
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean
import com.zhiyicx.tspay.TSPayClient

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

interface IntegrationRechargeContract {

    interface View : IBaseView<Presenter> {

        val money: Double
        fun payCredentialsResult(payStrV2Bean: PayStrV2Bean,amount: Double)
        fun configSureBtn(enable: Boolean)
        fun rechargeSuccess(amount: Double)
        fun initmRechargeInstructionsPop()

        fun useInputMonye(): Boolean
        /**
         * 更新积分配置信息
         */
        fun updateIntegrationConfig(isGetSuccess: Boolean, data: IntegrationConfigBean?)

        /**
         * handle request loading
         *
         * @param isShow true ,show loading
         */
        fun handleLoading(isShow: Boolean)
    }

    interface Presenter : IBaseTouristPresenter {

        fun getPayStr(@TSPayClient.PayKey channel: String, amount: Double)
        fun rechargeSuccess(charge: String,amount: Double)
        fun rechargeSuccessCallBack(charge: String,amount: Double)
        fun getIntegrationConfigBean()
    }
}
