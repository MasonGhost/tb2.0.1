package com.zhiyicx.thinksnsplus.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.umeng.socialize.UMShareAPI;
import com.zhiyicx.baseproject.base.TSActivity;
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
        checkIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIntent(intent);
    }

    private void checkIntent(Intent intent) {
        if (intent != null && intent.getBooleanExtra(BUNDLE_TOURIST_LOGIN, false)) {
            mIsTourstLogin = true;
        }
    }

    @Override
    protected LoginFragment getFragment() {
        return LoginFragment.newInstance(getIntent().getBooleanExtra(BUNDLE_TOURIST_LOGIN, false));
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
            finish();
        } else {
            ActivityUtils.goHome(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
