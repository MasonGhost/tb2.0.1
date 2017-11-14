package com.zhiyicx.thinksnsplus.modules.circle.detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class ChannelDetailActivity extends TSActivity<ChannelDetailPresenter, ChannelDetailFragment> {

    @Override
    protected void componentInject() {
        DaggerChannelDetailPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .channelDetailPresenterModule(new ChannelDetailPresenterModule(mContanierFragment))
                .shareModule(new ShareModule(this))
                .build().inject(this);
    }

    @Override
    protected ChannelDetailFragment getFragment() {
        return ChannelDetailFragment.newInstance(getIntent().getExtras());
    }
}
