package com.zhiyicx.thinksnsplus.modules.channel.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class ChannelDetailActivity extends TSActivity<ChannelDetailPresenter, ChannelDetailFragment> {

    @Override
    protected void componentInject() {
        DaggerChannelDetailPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .channelDetailPresenterModule(new ChannelDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected ChannelDetailFragment getFragment() {
        return ChannelDetailFragment.newInstance(getIntent().getExtras());
    }
}
