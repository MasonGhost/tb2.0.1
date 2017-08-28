package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class PublishContentActivity extends TSActivity<PublishContentPresenter, PublishContentFragment> {

    @Override
    protected PublishContentFragment getFragment() {
        return PublishContentFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerPublishContentComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .publishContentPresenterModule(new PublishContentPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }
}
