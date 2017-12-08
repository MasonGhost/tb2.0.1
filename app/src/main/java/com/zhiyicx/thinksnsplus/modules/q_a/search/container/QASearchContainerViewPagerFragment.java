package com.zhiyicx.thinksnsplus.modules.q_a.search.container;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby.FindSomeOneNearbyListFragment;
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
public class QASearchContainerViewPagerFragment extends TSViewPagerFragment {

    private String mCurrentSearchContent = "";

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
        titles.add(getString(R.string.qa_search));
        titles.add(getString(R.string.qa_search_topic));
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
            mFragmentList.add(QASearchListFragment.newInstance(getArguments()));
            mFragmentList.add(QATopicSearchListFragment.newInstance(getArguments()));
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

    public static QASearchContainerViewPagerFragment initFragment(Bundle bundle) {
        QASearchContainerViewPagerFragment fragment = new QASearchContainerViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
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
