<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.view.KeyEvent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter;

/**
 * @Describe 关于我们
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class AboutUsActivity extends TSActivity<RegisterPresenter, AboutUsFragment> {

    @Override
    protected void componentInject() {

    }

    @Override
    protected AboutUsFragment getFragment() {
        return AboutUsFragment.newInstance();
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
        if (mContanierFragment.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
=======
package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.view.KeyEvent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter;

/**
 * @Describe 关于我们
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class AboutUsActivity extends TSActivity<RegisterPresenter, AboutUsFragment> {

    @Override
    protected void componentInject() {

    }

    @Override
    protected AboutUsFragment getFragment() {
        return AboutUsFragment.newInstance();
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
        if (mContanierFragment.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
