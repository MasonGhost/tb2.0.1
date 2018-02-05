package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge


import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@Module
class IntegrationRechargePresenterModule(private val mView: IntegrationRechargeContract.View) {

    @Provides
    internal fun provideContractView(): IntegrationRechargeContract.View {
        return mView
    }

}
