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

import static com.zhiyicx.thinksnsplus.modules.channel.list.ChannelListViewPagerFragment.PAGE_ALL_CHANNEL_LIST;

/**
 * @author LiuChao
 * @describe 页面数据加载逻辑：如果本地数据为空，则从网络加载数据，否则，本次加载上次网络请求的数据，本次的网络请求数据会保存到数据库中，供下次显示
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
        if (mListDatas.isEmpty()) {
            // 如果界面数据为空,加载数据到界面
            super.onNetResponseSuccess(data, isLoadMore);
            // 如果界面上没有显示数据，从网络获取后界面上仍然没有数据，就切换到所有频道的页面
            if (data == null || data.isEmpty()) {
                ChannelListViewPagerFragment channelListViewPagerFragment = (ChannelListViewPagerFragment) getParentFragment();
                if (channelListViewPagerFragment != null) {
                    channelListViewPagerFragment.setSelectPager(PAGE_ALL_CHANNEL_LIST);
                }
            }
        } else {
            // 如果界面数据不为空，网络请求获取到的数据，那就下次加载
        }
        closeLoadingView();
    }

    @Override
    public void onCacheResponseSuccess(@NotNull List<ChannelSubscripBean> data, boolean isLoadMore) {
        super.onCacheResponseSuccess(data, isLoadMore);
        if (mListDatas.isEmpty()) {
            // 数据库数据为空，还需要从网络请求数据，这时还不能够关闭loadingview
        } else {
            closeLoadingView();
        }
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
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
