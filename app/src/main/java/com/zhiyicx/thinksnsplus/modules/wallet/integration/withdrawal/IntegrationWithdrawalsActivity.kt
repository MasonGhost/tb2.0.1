package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal

import android.content.Intent
import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.base.AppApplication

/**
 * @Describe 积分充值
 * @Author Jungle68
 * @Date 2018/1/18
 * @Contact master.jungle68@gmail.com
 */
class IntegrationWithdrawalsActivity : TSActivity<IntegrationWithdrawalsPresenter, IntegrationWithdrawalsFragment>() {


    override fun componentInject() {
        DaggerIntegrationWithdrawalsComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .integrationWithdrawalsPresenterModule(IntegrationWithdrawalsPresenterModule(mContanierFragment))
                .build()
                .inject(this)

    }

    override fun getFragment(): IntegrationWithdrawalsFragment {
        return IntegrationWithdrawalsFragment.newInstance(intent.extras)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mContanierFragment.onActivityResult(requestCode, resultCode, data)
    }
}
