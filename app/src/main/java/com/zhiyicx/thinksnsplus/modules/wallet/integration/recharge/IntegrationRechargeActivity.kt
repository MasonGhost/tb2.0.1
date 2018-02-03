package com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge

import android.content.Intent
import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.base.AppApplication
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationFragment
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationPresenter
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationPresenterModule

/**
 * @Describe 积分充值
 * @Author Jungle68
 * @Date 2018/1/18
 * @Contact master.jungle68@gmail.com
 */
class IntegrationRechargeActivity : TSActivity<IntegrationRechargePresenter, IntegrationRechargeFragment>() {


    override fun componentInject() {
        DaggerIntegrationRechargeComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .integrationRechargePresenterModule(IntegrationRechargePresenterModule(mContanierFragment))
                .build()
                .inject(this)

    }

    override fun getFragment(): IntegrationRechargeFragment {
        return IntegrationRechargeFragment.newInstance(intent.extras)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mContanierFragment.onActivityResult(requestCode, resultCode, data)
    }
}
