package com.zhiyicx.baseproject.base;

import android.support.v4.app.Fragment;
import android.widget.Toolbar;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseActivity;
import com.zhiyicx.common.utils.ActivityUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSActivity extends BaseActivity {
   protected Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ts;
    }

    @Override
    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), getFragment(), R.id.fl_fragment_container);
    }

    @Override
    protected void initData() {

    }

    /**
     * @return 当前页的Fragment
     */
    protected abstract Fragment getFragment();
}
