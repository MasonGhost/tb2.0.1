package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class ChangePasswordActivity extends TSActivity<ChangePasswordPresenter, ChangePasswordFragment> {
    @Override
    protected void componentInject() {
        DaggerChangePasswordComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .changePasswordPresenterModule(new ChangePasswordPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected ChangePasswordFragment getFragment() {
        return ChangePasswordFragment.newInstance();
    }

}
