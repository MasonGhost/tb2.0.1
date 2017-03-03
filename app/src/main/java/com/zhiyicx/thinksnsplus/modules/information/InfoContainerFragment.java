package com.zhiyicx.thinksnsplus.modules.information;

import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoContainerFragment extends TSViewPagerFragment {

    @Override
    protected List<String> initTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        return titles;
    }

    @Override
    protected List<Fragment> initFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW));
        return fragments;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTsvToolbar.setLeftImg(0);//不需要返回键
        mTsvToolbar.setRightImg(R.mipmap.sec_nav_arrow, R.mipmap.arrow_bg);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.information);
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_search;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }
}
