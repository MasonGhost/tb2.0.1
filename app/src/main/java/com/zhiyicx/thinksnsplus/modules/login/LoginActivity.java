package com.zhiyicx.thinksnsplus.modules.login;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author LiuChao
 * @describe 登陆界面
 * @date 2016/12/23
 * @contact email:450127106@qq.com
 */

public class LoginActivity extends TSActivity<LoginPresenter, LoginFragment> {
    @Override
    protected LoginFragment getFragment() {
        return new LoginFragment();
    }

    @Override
    protected void componentInject() {
        DaggerLoginComponent.builder().appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .loginPresenterModule(new LoginPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
