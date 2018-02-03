package com.zhiyicx.thinksnsplus.modules.wallet.recharge

import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@Module
class RechargePresenterModule(private val mView: RechargeContract.View) {

    @Provides
    internal fun provideRechargeContractView(): RechargeContract.View {
        return mView
    }

}
