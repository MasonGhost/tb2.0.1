package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter;

/**
 * @Describe 关于我没
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class AboutUsActivity extends TSActivity<RegisterPresenter, AboutUsFragment> {
    public static final String KEY_URL = "url";

    @Override
    protected void componentInject() {
    }

    @Override
    protected AboutUsFragment getFragment() {
        return AboutUsFragment.newInstance(getIntent().getStringExtra(KEY_URL));
    }
}
