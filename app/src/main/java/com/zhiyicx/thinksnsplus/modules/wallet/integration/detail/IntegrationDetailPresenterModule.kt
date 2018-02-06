package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail

import dagger.Module
import dagger.Provides

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
class IntegrationDetailPresenterModule(internal var mView: IntegrationDetailContract.View) {

    @Provides
    internal fun provideContractView(): IntegrationDetailContract.View {
        return mView
    }

}
