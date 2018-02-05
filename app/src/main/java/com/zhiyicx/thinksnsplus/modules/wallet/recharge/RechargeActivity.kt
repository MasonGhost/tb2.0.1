package com.zhiyicx.thinksnsplus.modules.wallet.recharge

import android.content.Intent

import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.base.AppApplication

/**
 * @Describe 钱包
 * @Author Jungle68
 * @Date 2017/5/22
 * @Contact master.jungle68@gmail.com
 */
class RechargeActivity : TSActivity<RechargePresenter, RechargeFragment>() {


    override fun componentInject() {
        DaggerRechargeComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .rechargePresenterModule(RechargePresenterModule(mContanierFragment))
                .build()
                .inject(this)

    }

    override fun getFragment(): RechargeFragment {
        return RechargeFragment.newInstance(intent.extras)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mContanierFragment.onActivityResult(requestCode, resultCode, data)
    }
}
