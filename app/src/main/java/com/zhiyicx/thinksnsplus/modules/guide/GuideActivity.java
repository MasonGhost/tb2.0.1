package com.zhiyicx.thinksnsplus.modules.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class GuideActivity extends TSActivity {

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getFragment() {
        return new GuideFragment();
    }

    @Override
    protected void componentInject() {

    }
}
