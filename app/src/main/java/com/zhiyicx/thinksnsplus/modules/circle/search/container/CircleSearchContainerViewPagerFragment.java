package com.zhiyicx.thinksnsplus.modules.circle.search.container;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.BaseCircleDetailFragment;
import com.zhiyicx.thinksnsplus.modules.circle.search.SearchCircleFragment;
import com.zhiyicx.thinksnsplus.modules.circle.search.SearchCirclePostFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.search.list.ISearchListener;
import com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa.QASearchListFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.search.list.topic.QATopicSearchListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/18
 * @Contact master.jungle68@gmail.com
 */
public class CircleSearchContainerViewPagerFragment extends TSViewPagerFragment {
    public static final String BUNDLE_PAGE = "PAGE";

    public static final int PAGE_CIRCLE = 0;
    public static final int PAGE_CIRCLE_POST = 1;

    private String mCurrentSearchContent = "";


    public static CircleSearchContainerViewPagerFragment initFragment(Bundle bundle) {
        CircleSearchContainerViewPagerFragment fragment = new CircleSearchContainerViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTsvToolbar.setLeftImg(0);
        if (getArguments() != null) {
            mVpFragment.setCurrentItem(getArguments().getInt(BUNDLE_PAGE));
        }
        mVpFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                onSearhChanged(mCurrentSearchContent);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected List<String> initTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.group_collect_dynamic));
        titles.add(getString(R.string.circle_post));
        return titles;
    }

    @Override
    protected boolean isAdjustMode() {
        return true;
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            mFragmentList.add(SearchCircleFragment.newInstance(false));
            mFragmentList.add(SearchCirclePostFragment.newInstance(BaseCircleRepository.CircleMinePostType.SEARCH));
        }
        return mFragmentList;
    }

    /**
     * 当前显示页切换
     *
     * @param position
     */
    public void setCurrentItem(int position) {

        mVpFragment.setCurrentItem(position);// 设置进入页面时，切换到关注还是粉丝列表

    }

    /**
     * 搜索内容变化
     *
     * @param string
     */
    public void onSearhChanged(String string) {
        this.mCurrentSearchContent = string;
        if (tsViewPagerAdapter == null || mVpFragment == null || TextUtils.isEmpty(string)) {
            return;
        }
        try {
            ((ISearchListener) tsViewPagerAdapter.getItem(mVpFragment.getCurrentItem())).onEditChanged(string);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
