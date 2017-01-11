package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.password.DaggerPasswordComponent;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class ChangePasswordActivity extends TSActivity<ChangePasswordPresenter, ChangePasswordFragment> {
    public static final String intent_password_type = "password_type";

    static public enum PASSWORD {
        CHANGE_PASSWORD,
        FIND_PASSWORD;
    }

    @Override
    protected void componentInject() {
        DaggerPasswordComponent
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
