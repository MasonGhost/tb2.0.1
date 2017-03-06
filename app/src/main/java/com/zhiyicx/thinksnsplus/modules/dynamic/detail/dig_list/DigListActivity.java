package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class DigListActivity extends TSActivity<DigListPresenter, DigListFragment> {

    @Override
    protected void componentInject() {
        DaggerDigListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .digListPresenterModule(new DigListPresenterModule((DigListContract.View) mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected DigListFragment getFragment() {
        return DigListFragment.initFragment(getIntent().getExtras());
    }
}
