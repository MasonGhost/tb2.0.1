package com.zhiyicx.thinksnsplus.modules.guide;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.R;

public class GuideActivity extends TSActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void ComponentInject() {

    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected Fragment getFragment() {
        return GuideFragment.newInstance("","");
    }
}
