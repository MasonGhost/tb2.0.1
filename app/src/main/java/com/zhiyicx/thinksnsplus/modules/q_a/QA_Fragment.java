package com.zhiyicx.thinksnsplus.modules.q_a;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container.QA$InfoContainerFragment;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/25/11:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_Fragment extends TSViewPagerFragment{

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.quiz), getString(R.string.qa_topic));
    }

    @Override
    protected List<Fragment> initFragments() {
        return Arrays.asList(QA$InfoContainerFragment.getInstance(), QA$InfoContainerFragment.getInstance());
    }

    @Override
    protected void initData() {

    }
}
