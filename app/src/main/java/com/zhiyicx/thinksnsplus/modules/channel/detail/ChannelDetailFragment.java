package com.zhiyicx.thinksnsplus.modules.channel.detail;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public class ChannelDetailFragment extends TSListFragment<ChannelDetailContract.Presenter, DynamicBean> implements ChannelDetailContract.View {
    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    public static ChannelDetailFragment newInstance(Bundle bundle) {
        ChannelDetailFragment channelDetailFragment = new ChannelDetailFragment();
        channelDetailFragment.setArguments(bundle);
        return channelDetailFragment;
    }
}
