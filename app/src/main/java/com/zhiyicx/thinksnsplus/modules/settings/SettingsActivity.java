package com.zhiyicx.thinksnsplus.modules.settings;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter;

/**
 * @Describe 设置
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */

public class SettingsActivity extends TSActivity<RegisterPresenter, SettingsFragment> {

    @Override
    protected void componentInject() {
//        DaggerRegisterComponent
//                .builder()
//                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
//                .registerPresenterModule(new RegisterPresenterModule(mContanierFragment))
//                .build()
//                .inject(this);

    }

    @Override
    protected SettingsFragment getFragment() {
        return SettingsFragment.newInstance();
    }

}
