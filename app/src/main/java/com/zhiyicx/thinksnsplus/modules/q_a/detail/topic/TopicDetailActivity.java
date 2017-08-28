package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */

public class TopicDetailActivity extends TSActivity<TopicDetailPresenter, TopicDetailFragment>{

    public static final String BUNDLE_TOPIC_BEAN = "bundle_topic_bean";

    @Override
    protected TopicDetailFragment getFragment() {
        return new TopicDetailFragment().instance(getIntent().getBundleExtra(BUNDLE_TOPIC_BEAN));
    }

    @Override
    protected void componentInject() {
        DaggerTopicDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .topicDetailPresenterModule(new TopicDetailPresenterModule(mContanierFragment))
                .shareModule(new ShareModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }
}
