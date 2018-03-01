package com.zhiyicx.thinksnsplus.modules.tb.share

import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
class DynamicSharePresenterModule(private val mView: DynamicShareContract.View) {

    @Provides
    internal fun provideContractView(): DynamicShareContract.View {
        return mView
    }

}
