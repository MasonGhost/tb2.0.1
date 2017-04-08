package com.zhiyicx.thinksnsplus.modules.channel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;

public class ChannelListActivity extends TSActivity<ChannelListPresenter, ChannelListViewPagerFragment>  {

    @Override
    protected ChannelListViewPagerFragment getFragment() {
        return null;
    }

    @Override
    protected void componentInject() {

    }
}
