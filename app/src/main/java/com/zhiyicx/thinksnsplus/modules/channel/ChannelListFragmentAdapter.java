package com.zhiyicx.thinksnsplus.modules.channel;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/10
 * @contact email:450127106@qq.com
 */

public class ChannelListFragmentAdapter extends CommonAdapter<ChannelSubscripBean> {
    private ChannelListContract.Presenter mPresenter;

    public ChannelListFragmentAdapter(Context context, int layoutId, List<ChannelSubscripBean> datas, ChannelListContract.Presenter presenter) {
        super(context, layoutId, datas);
        mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, ChannelSubscripBean channelSubscripBean, int position) {

    }
}
