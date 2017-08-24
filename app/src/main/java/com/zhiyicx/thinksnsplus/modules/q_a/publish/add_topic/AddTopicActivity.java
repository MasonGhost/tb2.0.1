package com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;


/**
 * @Describe the page for publish qustion  topic
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

public class AddTopicActivity extends TSActivity<AddTopicPresenter, AddTopicFragment> {
    @Override
    protected AddTopicFragment getFragment() {
        return AddTopicFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerAddTopicComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .addTopicPresenterModule(new AddTopicPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }
}
