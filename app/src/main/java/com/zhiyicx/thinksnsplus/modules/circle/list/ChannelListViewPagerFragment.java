package com.zhiyicx.thinksnsplus.modules.circle.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public class ChannelListViewPagerFragment extends TSViewPagerFragment<ChannelListContract.Presenter> {

    /**
     * 页面类型
     */
    public static final int PAGE_MY_SUBSCRIB_CHANNEL_LIST = 0;
    public static final int PAGE_ALL_CHANNEL_LIST = 1;
    public static final String PAGE_TYPE = "page_type";

    @Inject
    ChannelListPresenter mChannelListPresenter;

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.joined_group), getString(R.string.all_group));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            Bundle subscripChannelBundle = new Bundle();
            subscripChannelBundle.putInt(PAGE_TYPE, PAGE_MY_SUBSCRIB_CHANNEL_LIST);
            Fragment subscripChannelFragment = ChannelListFragment.newInstance(subscripChannelBundle);
            Bundle allChannelBundle = new Bundle();
            allChannelBundle.putInt(PAGE_TYPE, PAGE_ALL_CHANNEL_LIST);
            Fragment allChannelFragment = ChannelListFragment.newInstance(allChannelBundle);
            mFragmentList = Arrays.asList(subscripChannelFragment, allChannelFragment);
        }
        return mFragmentList;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mVpFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ChannelListFragment fragment = (ChannelListFragment) mFragmentList.get(0);
                    if (fragment.handleTouristControl()) {
                        setSelectPager(PAGE_ALL_CHANNEL_LIST);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static ChannelListViewPagerFragment newInstance(Bundle bundle) {
        ChannelListViewPagerFragment channelListViewPagerFragment = new ChannelListViewPagerFragment();
        channelListViewPagerFragment.setArguments(bundle);
        return channelListViewPagerFragment;
    }

    /**
     * 设置选择的页面
     *
     * @param page
     */
    public void setSelectPager(int page) {
        mVpFragment.setCurrentItem(page, false);
    }
}
