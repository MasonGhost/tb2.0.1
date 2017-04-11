package com.zhiyicx.thinksnsplus.modules.channel.list;

import com.zhiyicx.baseproject.base.TSActivity;

public class ChannelListActivity extends TSActivity<ChannelListPresenter, ChannelListViewPagerFragment> {

    @Override
    protected ChannelListViewPagerFragment getFragment() {
        return ChannelListViewPagerFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        
    }
}
