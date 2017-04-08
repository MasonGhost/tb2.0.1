package com.zhiyicx.thinksnsplus.modules.channel;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public class ChannelListFragment extends TSListFragment<ChannelListContract.Presenter, ChannelSubscripBean>
        implements ChannelListContract.View {
    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
