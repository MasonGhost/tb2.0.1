package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container;

import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist.QATopicListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QATopicFragmentContainerFragment extends TSViewPagerFragment {

    private List<Fragment> mFragments;
    public static final String TOPIC_TYPE = "-1";

    public static QATopicFragmentContainerFragment getInstance() {
        return new QATopicFragmentContainerFragment();
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.qa_topic_all), getString(R.string.qa_topic_user_followed));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(QATopicListFragment.newInstance(TOPIC_TYPE));
            mFragments.add(QATopicListFragment.newInstance(TOPIC_TYPE));
        }
        return mFragments;
    }

    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);
        mTsvToolbar.setLeftImg(0);
    }

    @Override
    protected void initData() {

    }
}
