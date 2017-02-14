package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class FollowFansListActivity extends TSActivity<FollowFansListPresenter, FollowFansViewPagerFragment> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void componentInject() {
    }

    @Override
    protected FollowFansViewPagerFragment getFragment() {
        return FollowFansViewPagerFragment.initFragment(getIntent().getExtras());
    }
}
