<<<<<<< HEAD
package com.zhiyicx.baseproject.base;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseActivity;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityUtils;

/**
 * @Describe activity只是作为fragment的容器，具体的功能逻辑在fragment中完成
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSActivity<P extends BasePresenter,F extends Fragment> extends BaseActivity<P> {

    protected F mContanierFragment;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ts;
    }

    @Override
    protected void initView() {
        // 添加fragment
        mContanierFragment= getFragment();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mContanierFragment, R.id.fl_fragment_container);
    }

    @Override
    protected void initData() {

    }

    /**
     * @return 当前页的Fragment
     */
    protected abstract F getFragment();

}
=======
package com.zhiyicx.baseproject.base;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseActivity;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityUtils;

/**
 * @Describe activity只是作为fragment的容器，具体的功能逻辑在fragment中完成
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSActivity<P extends BasePresenter,F extends Fragment> extends BaseActivity<P> {

    protected F mContanierFragment;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ts;
    }

    @Override
    protected void initView() {
        // 添加fragment
        mContanierFragment= getFragment();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mContanierFragment, R.id.fl_fragment_container);
    }

    @Override
    protected void initData() {

    }

    /**
     * @return 当前页的Fragment
     */
    protected abstract F getFragment();

}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
