package com.zhiyicx.zhibolibrary.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;

import java.util.List;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<ZBLBaseFragment> mFragments;
    private List<String> mFragmentTitles;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void bindData(List<ZBLBaseFragment> fragments, List<String> titles) {
        this.mFragments = fragments;
        this.mFragmentTitles = titles;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
