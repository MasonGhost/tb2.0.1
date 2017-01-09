package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
}
