package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.amap.api.services.core.LatLonPoint;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby.FindSomeOneNearbyListFragment;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/18
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneContainerViewPagerFragment extends TSViewPagerFragment {

    public static final int PAGE_POSITION_HOT = 0;
    public static final int PAGE_POSITION_NEW = 1;
    public static final int PAGE_POSITION_RECOMMEND = 2;
    public static final int PAGE_POSITION_NEARBY = 3;


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTsvToolbar.setLeftImg(0);
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected List<String> initTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        titles.add(getString(R.string.info_recommend));
        titles.add(getString(R.string.neary_by));
        return titles;
    }

    @Override
    protected List<Fragment> initFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        getFragment(fragmentList, FindSomeOneListFragment.TYPE_HOT);
        getFragment(fragmentList, FindSomeOneListFragment.TYPE_NEW);
        getFragment(fragmentList, FindSomeOneListFragment.TYPE_RECOMMENT);
        fragmentList.add(FindSomeOneNearbyListFragment.initFragment());
        return fragmentList;
    }

    private void getFragment(List<Fragment> fragmentList, int type) {
        Bundle bundle1 = new Bundle();
        bundle1.putInt(FindSomeOneListFragment.PAGE_TYPE, type);
        fragmentList.add(FindSomeOneListFragment.initFragment(bundle1));
    }

    /**
     * 当前显示页切换
     *
     * @param position
     */
    public void setCurrentItem(int position, boolean smoothScroll) {
        // 设置进入页面时，切换到关注还是粉丝列表
        mVpFragment.setCurrentItem(position, smoothScroll);

    }

    public static FindSomeOneContainerViewPagerFragment initFragment(Bundle bundle) {
        FindSomeOneContainerViewPagerFragment fragment = new FindSomeOneContainerViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
