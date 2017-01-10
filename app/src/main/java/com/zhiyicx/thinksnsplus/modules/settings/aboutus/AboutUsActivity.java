package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.view.KeyEvent;

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

    /**
     * 覆盖系统的回退键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mContanierFragment.onKeyDown(keyCode, event) == true ? true : super.onKeyDown(keyCode, event);
    }
}
