package com.zhiyicx.thinksnsplus.modules.settings

import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.base.AppApplication

/**
 * @Describe 设置
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */

class SettingsActivity : TSActivity<SettingsPresenter, SettingsFragment>() {

    override fun componentInject() {
        DaggerSettingsComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .settingsPresenterModule(SettingsPresenterModule(mContanierFragment))
                .build()
                .inject(this)

    }

    override fun getFragment(): SettingsFragment {
        return SettingsFragment.newInstance()
    }
}

