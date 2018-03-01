package com.zhiyicx.thinksnsplus.modules.dynamic.share

import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.base.AppApplication
import com.zhiyicx.thinksnsplus.modules.dynamic.list.TBDynamicFragment

/**
 * @Describe 填写邀请码
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


}

