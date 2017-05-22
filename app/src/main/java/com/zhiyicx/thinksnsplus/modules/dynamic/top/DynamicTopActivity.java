package com.zhiyicx.thinksnsplus.modules.dynamic.top;

import com.zhiyicx.baseproject.base.TSActivity;

public class DynamicTopActivity extends TSActivity {

    @Override
    protected DynamicTopFragment getFragment() {
        return new DynamicTopFragment();
    }

    @Override
    protected void componentInject() {

    }
}
