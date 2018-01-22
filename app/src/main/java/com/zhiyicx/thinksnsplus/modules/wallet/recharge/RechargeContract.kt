package com.zhiyicx.thinksnsplus.modules.wallet.recharge

import com.zhiyicx.baseproject.base.IBaseTouristPresenter
import com.zhiyicx.common.mvp.i.IBaseView
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean
import com.zhiyicx.tspay.TSPayClient

import retrofit2.http.Path
import rx.Observable

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

interface RechargeContract {

    interface View : IBaseView<Presenter> {
        val money: Double
        fun payCredentialsResult(payStrBean: PayStrBean)
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
