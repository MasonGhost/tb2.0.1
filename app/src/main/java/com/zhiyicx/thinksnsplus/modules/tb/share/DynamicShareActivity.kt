package com.zhiyicx.thinksnsplus.modules.tb.share

import android.content.Intent
import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl
import com.zhiyicx.thinksnsplus.base.AppApplication
import com.zhiyicx.thinksnsplus.modules.dynamic.list.TBDynamicFragment

/**
 * @Describe 快讯分享
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */

class DynamicShareActivity : TSActivity<DynamicSharePresenter, DynamicShareFragment>() {

    override fun componentInject() {


        DaggerDynamicShareComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicSharePresenterModule(DynamicSharePresenterModule(mContanierFragment))
                .build()
                .inject(this)

    }

    override fun getFragment(): DynamicShareFragment {
        return DynamicShareFragment.newInstance(intent.extras.getSerializable(TBDynamicFragment.BUNDLE_SHARE_DATA) as DynamicShareBean)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this)
        mContanierFragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        UmengSharePolicyImpl.onDestroy(this)
    }


}

