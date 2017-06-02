package com.zhiyicx.thinksnsplus.modules.feedback;

import com.zhiyicx.baseproject.base.TSActivity;

public class FeedBackActivity extends TSActivity {

    @Override
    protected FeedBackFragment getFragment() {
        return FeedBackFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }
}
