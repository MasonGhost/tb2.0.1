package com.zhiyicx.thinksnsplus.modules.q_a.search.container;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby.FindSomeOneNearbyListFragment;
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

    public static final int PAGE_POSITION_QA = 0;
    public static final int PAGE_POSITION_TOPIC = 1;



    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTsvToolbar.setLeftImg(0);

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
    protected List<Fragment> initFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(QASearchListFragment.newInstance(getArguments()));
        fragmentList.add(QATopicSearchListFragment.newInstance(getArguments()));
        return fragmentList;
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

}
