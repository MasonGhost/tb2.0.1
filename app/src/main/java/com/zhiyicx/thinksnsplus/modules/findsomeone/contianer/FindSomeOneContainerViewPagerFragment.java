package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FindSomeOneContainerViewPagerFragment extends TSViewPagerFragment {


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setCurrentItem();
    }

    @Override
    protected int setLeftImg() {
        return 0;
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
        getFragment(fragmentList, FindSomeOneListFragment.TYPE_NEARBY);
        return fragmentList;
    }

    private void getFragment(List<Fragment> fragmentList,int type) {
        Bundle bundle1 = new Bundle();
        bundle1.putInt(FindSomeOneListFragment.PAGE_TYPE, type);
        fragmentList.add(FindSomeOneListFragment.initFragment(bundle1));
    }

    private void setCurrentItem() {
        Bundle bundle = getArguments();
        int pageType = bundle.getInt(FindSomeOneListFragment.PAGE_TYPE);// 当前进入的是关注还是粉丝列表
        mVpFragment.setCurrentItem(pageType);// 设置进入页面时，切换到关注还是粉丝列表
    }

    public static FindSomeOneContainerViewPagerFragment initFragment(Bundle bundle) {
        FindSomeOneContainerViewPagerFragment fragment = new FindSomeOneContainerViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
