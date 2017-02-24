package com.zhiyicx.thinksnsplus.modules.music_fm.music_album;


import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;


/**
 * @Author Jliuer
 * @Date 2017/02/21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicListActivityTest extends AcitivityTest {

    MusicListFragment mFragment;

    @Rule
    public ActivityTestRule<MusicListActivity> mActivityRule = new ActivityTestRule
            (MusicListActivity.class);

    @Before
    public void init() {
        mFragment = mActivityRule.getActivity().getFragment();
    }

    @Ignore
    public CommonAdapter getAdapter() {
        return mFragment.getAdapter();
    }

    @Test
    public void initData() {
//        mFragment.initData();
    }
}
