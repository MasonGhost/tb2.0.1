package com.zhiyicx.thinksnsplus.modules.register;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.login.LoginActivity.BUNDLE_TOURIST_LOGIN;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/22
 * @Contact master.jungle68@gmail.com
 */

public class RegisterActivity extends TSActivity<RegisterPresenter, RegisterFragment> {

    @Override
    protected void componentInject() {
        DaggerRegisterComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .registerPresenterModule(new RegisterPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected RegisterFragment getFragment() {
        return RegisterFragment.newInstance(getIntent().getBooleanExtra(BUNDLE_TOURIST_LOGIN, false));
    }

}
