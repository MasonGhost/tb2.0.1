package com.zhiyicx.thinksnsplus.modules.settings.init_password;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.base.BaseActivity;

/**
 * @author Catherine
 * @describe 初始化账号密码的页面，第三方登陆的账号，是没有密码的，需要先设置密码
 * @date 2017/9/18
 * @contact email:648129313@qq.com
 */

public class InitPasswordActivity extends TSActivity<InitPasswordPresenter, InitPasswordFragment>{

    @Override
    protected InitPasswordFragment getFragment() {
        return null;
    }

    @Override
    protected void componentInject() {

    }
}
