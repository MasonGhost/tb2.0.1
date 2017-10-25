package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.view.KeyEvent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class UserInfoActivity extends TSActivity<UserInfoPresenter, UserInfoFragment> {

    @Override
    protected UserInfoFragment getFragment() {
        return new UserInfoFragment();
    }

    @Override
    protected void componentInject() {
        DaggerUserInfoComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .userInfoPresenterModule(new UserInfoPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 处理fragment中的返回键按键
        boolean flag = mContanierFragment.onKeyDown(keyCode, event);
        return flag || super.onKeyDown(keyCode, event);
    }

}
