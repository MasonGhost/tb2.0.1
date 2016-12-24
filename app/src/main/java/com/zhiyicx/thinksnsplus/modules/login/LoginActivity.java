package com.zhiyicx.thinksnsplus.modules.login;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @author LiuChao
 * @describe 登陆界面
 * @date 2016/12/23
 * @contact email:450127106@qq.com
 */

public class LoginActivity extends TSActivity {
    @Override
    protected Fragment getFragment() {
        return new LoginFragment();
    }

}
