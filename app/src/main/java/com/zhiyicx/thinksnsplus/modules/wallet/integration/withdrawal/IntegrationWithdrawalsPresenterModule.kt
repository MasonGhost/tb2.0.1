package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal


import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@Module
class IntegrationWithdrawalsPresenterModule(private val mView: IntegrationWithdrawalsContract.View) {

    @Provides
    internal fun provideContractView(): IntegrationWithdrawalsContract.View {
        return mView
    }

}
