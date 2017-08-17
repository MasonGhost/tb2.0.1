package com.zhiyicx.thinksnsplus.modules.diglist;

import com.zhiyicx.baseproject.base.TSActivity;

public class DigListActivity extends TSActivity<DigListPresenter, DigListFragment> {

    @Override
    protected void componentInject() {

    }

    @Override
    protected DigListFragment getFragment() {
        return DigListFragment.initFragment(getIntent().getExtras());
    }
}
