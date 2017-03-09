package com.zhiyicx.thinksnsplus.modules.home.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe 主页MainFragment
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MainFragment extends TSViewPagerFragment {
    public void setOnImageClickListener(DynamicFragment.OnCommentClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    DynamicFragment.OnCommentClickListener mOnCommentClickListener;
    public static MainFragment newInstance(DynamicFragment.OnCommentClickListener l) {
        MainFragment fragment = new MainFragment();
        fragment.setOnImageClickListener(l);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTsvToolbar.setLeftImg(0);//不需要返回键
    }

    @Override
    protected void initData() {

    }

    @Override
    protected List<String> initTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        return titles;
    }

    @Override
    protected List<Fragment> initFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS,mOnCommentClickListener));
        fragments.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS,mOnCommentClickListener));
        fragments.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW,mOnCommentClickListener));
        return fragments;
    }
}
