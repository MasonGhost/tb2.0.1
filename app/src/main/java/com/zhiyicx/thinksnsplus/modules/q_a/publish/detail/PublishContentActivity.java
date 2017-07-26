package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class PublishContentActivity extends TSActivity<PublishContentPresenter, PublishContentFragment> {

    @Override
    protected PublishContentFragment getFragment() {
        return PublishContentFragment.newInstance(new Bundle());
    }

    @Override
    protected void componentInject() {
        DaggerPublishContentComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .publishContentPresenterModule(new PublishContentPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
