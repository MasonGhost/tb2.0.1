package com.zhiyicx.thinksnsplus.modules.feedback;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class FeedBackActivity extends TSActivity<FeedBackPresenter, FeedBackFragment> {

    @Override
    protected FeedBackFragment getFragment() {
        return FeedBackFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerFeedBackComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .feedBackPresenterModule(new FeedBackPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
