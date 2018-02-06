package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansViewPagerFragment extends TSViewPagerFragment<FollowFansListContract.Presenter> {


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setCurrentItem();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected List<String> initTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.fans));
        titles.add(getString(R.string.follow));
        return titles;
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            long userId = getArguments().getLong(FollowFansListFragment.PAGE_DATA);
            mFragmentList = new ArrayList<>();
            Bundle bundle1 = new Bundle();
            bundle1.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FANS_FRAGMENT_PAGE);
            bundle1.putLong(FollowFansListFragment.PAGE_DATA, userId);
            mFragmentList.add(FollowFansListFragment.initFragment(bundle1));
            Bundle bundle2 = new Bundle();
            bundle2.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FOLLOW_FRAGMENT_PAGE);
            bundle2.putLong(FollowFansListFragment.PAGE_DATA, userId);
            mFragmentList.add(FollowFansListFragment.initFragment(bundle2));
        }
        return mFragmentList;
    }

    private void setCurrentItem() {
        Bundle bundle = getArguments();
        int pageType = bundle.getInt(FollowFansListFragment.PAGE_TYPE);// 当前进入的是关注还是粉丝列表
        mVpFragment.setCurrentItem(pageType);// 设置进入页面时，切换到关注还是粉丝列表
    }

    public static FollowFansViewPagerFragment initFragment(Bundle bundle) {
        FollowFansViewPagerFragment followFansViewPagerFragment = new FollowFansViewPagerFragment();
        followFansViewPagerFragment.setArguments(bundle);
        return followFansViewPagerFragment;
    }

    @Override
    protected int getOffsetPage() {
        return 2;
    }
}
