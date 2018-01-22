package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine


import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@Module
class MineIntegrationPresenterModule(private val mView: MineIntegrationContract.View) {

    @Provides
    internal fun provideContractView(): MineIntegrationContract.View {
        return mView
    }

}
