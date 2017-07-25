package com.zhiyicx.thinksnsplus.modules.q_a;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/25/11:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA$Fragment extends TSViewPagerFragment{


    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.quiz), getString(R.string.qa_topic));
    }

    @Override
    protected List<Fragment> initFragments() {
        return null;
    }

    @Override
    protected void initData() {

    }
}
