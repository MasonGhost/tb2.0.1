package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class DigListActivity extends TSActivity<DigListPresenter, DigListFragment> {

    @Override
    protected void componentInject() {
        DaggerDigListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .digListPresenterModule(new DigListPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected DigListFragment getFragment() {
        return DigListFragment.initFragment(getIntent().getExtras());
    }
}
