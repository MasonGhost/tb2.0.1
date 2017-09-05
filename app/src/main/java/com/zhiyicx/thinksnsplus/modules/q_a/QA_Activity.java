package com.zhiyicx.thinksnsplus.modules.q_a;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class QA_Activity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return new QA_Fragment();
    }

    @Override
    protected void componentInject() {

    }

}
