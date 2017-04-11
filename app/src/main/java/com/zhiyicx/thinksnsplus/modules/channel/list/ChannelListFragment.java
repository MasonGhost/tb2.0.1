package com.zhiyicx.thinksnsplus.modules.channel.list;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    private int pageType = 0;// 上一个Fragment传递过来的页面类型

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter<ChannelSubscripBean> commonAdapter = new ChannelListFragmentAdapter(getContext()
                , R.layout.item_channel_list, mListDatas, mPresenter);
        return commonAdapter;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        DaggerChannelListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .channelListPresenterModule(new ChannelListPresenterModule(this))
                .build().inject(this);
        super.initView(rootView);
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<ChannelSubscripBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        closeLoadingView();
    }

    @Override
    public void onCacheResponseSuccess(@NotNull List<ChannelSubscripBean> data, boolean isLoadMore) {
        super.onCacheResponseSuccess(data, isLoadMore);
        closeLoadingView();
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected void initData() {
        pageType = getArguments().getInt(ChannelListViewPagerFragment.PAGE_TYPE);
        super.initData();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    public static ChannelListFragment newInstance(Bundle bundle) {
        ChannelListFragment channelListFragment = new ChannelListFragment();
        channelListFragment.setArguments(bundle);
        return channelListFragment;
    }

    @Override
    public int getPageType() {
        return pageType;
    }

    @Override
    public void refreshSubscribState(int position) {
        refreshData(position);
    }

    @Override
    public void refreshSubscribState() {
        refreshData();
    }

    @Override
    public List<ChannelSubscripBean> getChannelListData() {
        return mListDatas;
    }
}
