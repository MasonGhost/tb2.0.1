package com.zhiyicx.baseproject.base;

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

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSActivity extends BaseActivity {
    private static final int DEFAULT_TOOLBAR = R.layout.toolbar_custom;

    protected Toolbar mToolbar;
    private TextView mToolbarLeft;
    private TextView mToolbarRight;
    private TextView mToolbarCenter;

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

    /**
     * 获取toolbar下方的布局文件
     *
     * @return
     */
    protected abstract int getBodyLayout();

    /**
     * 获取toolbar的布局文件,如果需要返回自定义的toolbar布局，重写该方法；否则默认返回缺省的t
     */
    protected int getToolBarLayout() {
        return DEFAULT_TOOLBAR;
    }

    /**
     * 是否显示toolbar,默认显示
     */
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected View getContentView() {
        View rootView = mLayoutInflater.inflate(R.layout.activity_ts, null);
        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.toolbar_layout);
        if (showToolbar()) {// 在需要显示toolbar时，进行添加
            View toolBarContainer = mLayoutInflater.inflate(getToolBarLayout(), null);
            initDefaultToolBar(toolBarContainer);
            appBarLayout.addView(toolBarContainer);
        }
        return rootView;
    }

    /**
     * 初始化默认的toolbar
     */
    protected void initDefaultToolBar(View toolBarContainer) {
        mToolbarLeft = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_left);
        mToolbarRight = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_center);
        mToolbarCenter = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_r);

        // 如果标题为空，就隐藏它
        mToolbarCenter.setVisibility(TextUtils.isEmpty(setCenterTitle()) ? View.GONE : View.VISIBLE);
        mToolbarLeft.setVisibility(TextUtils.isEmpty(setLeftTitle()) ? View.GONE : View.VISIBLE);
        mToolbarRight.setVisibility(TextUtils.isEmpty(setRightTitle()) ? View.GONE : View.VISIBLE);
        mToolbarCenter.setText(setCenterTitle());
        mToolbarLeft.setText(setLeftTitle());
        mToolbarRight.setText(setRightTitle());
    }

    /**
     * 设置中间的标题
     */
    protected String setCenterTitle() {
        return "";
    }

    /**
     * 设置左边的标题
     */
    protected String setLeftTitle() {
        return "";
    }

    /**
     * 设置右边的标题
     */
    protected String setRightTitle() {
        return "";
    }
}
