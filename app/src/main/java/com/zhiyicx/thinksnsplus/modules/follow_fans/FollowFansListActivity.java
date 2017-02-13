package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;

public class FollowFansListActivity extends TSActivity<FollowFansListPresenter,FollowFansListFragment> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void componentInject() {

    }

    @Override
    protected FollowFansListFragment getFragment() {
        return null;
    }
}
