package com.zhiyicx.baseproject.base;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseActivity;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.common.utils.StatusBarUtils;

/**
 * @Describe activity只是作为fragment的容器，具体的功能逻辑在fragment中完成
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ts;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setStatusBarColor(this, R.color.themeColor);
        // 添加fragment
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), getFragment(), R.id.fl_fragment_container);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void ComponentInject() {

    }

    /**
     * @return 当前页的Fragment
     */
    protected abstract Fragment getFragment();

}
