package com.zhiyicx.thinksnsplus.modules.dynamic.send.selectDynamicType;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class SelectDynamicTypeActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return new SelectDynamicTypeFragment();
    }

    @Override
    protected void componentInject() {

    }
}
