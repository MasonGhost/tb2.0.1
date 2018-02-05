package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine

import com.zhiyicx.baseproject.base.IBaseTouristPresenter
import com.zhiyicx.common.mvp.i.IBaseView
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

interface MineIntegrationContract {

    interface View : IBaseView<Presenter> {
        /**
         * update balance
         *
         * @param balance current user's balance
         */
        fun updateBalance(balance: Long)

        /**
         * handle request loading
         *
         * @param isShow true ,show loading
         */
        fun handleLoading(isShow: Boolean)

        /**
         * wallet callback
         *
         * @param configBean integration config info
         * @param tag              action tag, 1 recharge 2 withdraw
         */
        fun integrationConfigCallBack(configBean: IntegrationConfigBean, tag: Int)

    }

    interface Presenter : IBaseTouristPresenter {

        val tipPopRule: String

        /**
         *
         * @return advert  for  integration
         */
        val integrationAdvert: List<RealAdvertListBean>

        /**
         * update user info
         */
        fun updateUserInfo()

        /**
         * @return true when first looking wallet page
         */
        fun checkIsNeedTipPop(): Boolean

        /**
         * check wallet config info, if walletconfig has cach used it or get it from server
         *
         * @param tag action tag
         */
        fun checkIntegrationConfig(tag: Int, isNeedTip: Boolean)
    }
}
