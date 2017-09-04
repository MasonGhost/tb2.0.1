package com.zhiyicx.thinksnsplus.modules.information.my_info;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class ManuscriptsActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return ManuscriptContainerFragment.getInstance();
    }

    @Override
    protected void componentInject() {

    }
}
