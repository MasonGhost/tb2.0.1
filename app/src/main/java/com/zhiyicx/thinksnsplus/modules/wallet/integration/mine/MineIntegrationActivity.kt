package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine

import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.base.AppApplication

/**
 * @Describe 我的积分
 * @Author Jungle68
 * @Date 2018/1/18
 * @Contact master.jungle68@gmail.com
 */
class MineIntegrationActivity : TSActivity<MineIntegrationPresenter, MineIntegrationFragment>() {


    override fun componentInject() {
        DaggerMineIntegrationComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .mineIntegrationPresenterModule(MineIntegrationPresenterModule(mContanierFragment))
                .build()
                .inject(this)

    }

    override fun getFragment(): MineIntegrationFragment {
        return MineIntegrationFragment.newInstance()
    }

}
