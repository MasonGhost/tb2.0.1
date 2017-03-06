package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
