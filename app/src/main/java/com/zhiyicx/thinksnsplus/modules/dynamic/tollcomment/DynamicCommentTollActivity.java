package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import com.zhiyicx.baseproject.base.TSActivity;

public class DynamicCommentTollActivity extends TSActivity {

    @Override
    protected DynamicCommentTollFragment getFragment() {
        return DynamicCommentTollFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }
}
