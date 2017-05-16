package com.zhiyicx.thinksnsplus.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author LiuChao
 * @describe 登录界面
 * @date 2016/12/23
 * @contact email:450127106@qq.com
 */

public class LoginActivity extends TSActivity<LoginPresenter, LoginFragment> {
    public static final String BUNDLE_TOURIST_LOGIN = "bundle_tourist_login";

    private boolean mIsTourstLogin = false;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHandler.getInstance().removeCurrentTopActivity();// 清除 homeAcitivity 重新加载
        checkIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIntent(intent);
    }

    private void checkIntent(Intent intent) {
        if (intent!= null && intent.getBooleanExtra(BUNDLE_TOURIST_LOGIN,false)) {
            mIsTourstLogin = true;
        }
    }

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

    @Override
    public void onBackPressed() {
        if (mIsTourstLogin) {
            super.onBackPressed();
        } else {
            ActivityUtils.goHome(this);
        }
    }
}
