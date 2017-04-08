package com.zhiyicx.thinksnsplus.modules.channel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public class ChannelListFragment extends TSListFragment<ChannelListContract.Presenter, ChannelSubscripBean>
        implements ChannelListContract.View {
    @Inject
    ChannelListPresenter mChannelListPresenter;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        DaggerChannelListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .channelListPresenterModule(new ChannelListPresenterModule(this))
                .build();
        super.initView(rootView);
    }
}
