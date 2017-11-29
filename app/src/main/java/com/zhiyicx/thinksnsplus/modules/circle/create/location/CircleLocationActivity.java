package com.zhiyicx.thinksnsplus.modules.circle.create.location;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class CircleLocationActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return new CircleLocationFragment();
    }

    @Override
    protected void componentInject() {

    }
}
