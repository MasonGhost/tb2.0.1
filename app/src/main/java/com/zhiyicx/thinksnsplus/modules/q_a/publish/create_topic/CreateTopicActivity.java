package com.zhiyicx.thinksnsplus.modules.q_a.publish.create_topic;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class CreateTopicActivity extends TSActivity<CreateTopicPresenter, CreateTopicFragment> {

    @Override
    protected CreateTopicFragment getFragment() {
        return CreateTopicFragment.getInstance();
    }

    @Override
    protected void componentInject() {
        DaggerCreateTopicComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .createTopicPresenterModule(new CreateTopicPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
