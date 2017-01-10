package com.zhiyicx.thinksnsplus.modules.password;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class PasswordActivity extends TSActivity<PasswordPresenter, PasswordFragment> {
    static public enum PASSWORD {
        CHANGE_PASSWORD,
        FIND_PASSWORD;
    }

    @Override
    protected void componentInject() {
        DaggerPasswordComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .passwordPresenterModule(new PasswordPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected PasswordFragment getFragment() {
        return PasswordFragment.newInstance();
    }

}
