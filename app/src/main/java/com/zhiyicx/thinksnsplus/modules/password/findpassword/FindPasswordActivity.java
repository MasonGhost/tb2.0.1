package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 找回密码
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */

public class FindPasswordActivity extends TSActivity<FindPasswordPresenter, FindPasswordFragment> {

    @Override
    protected void componentInject() {
        DaggerFindPasswordComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .findPasswordPresenterModule(new FindPasswordPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected FindPasswordFragment getFragment() {
        return FindPasswordFragment.newInstance();
    }

}
