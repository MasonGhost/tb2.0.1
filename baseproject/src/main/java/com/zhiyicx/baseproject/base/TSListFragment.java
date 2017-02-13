package com.zhiyicx.baseproject.base;


import android.graphics.Canvas;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.TSPRefreshViewHolder;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;


/**
 * @Describe 基础列表类
 * @Author Jungle68
 * @Date 2017/2/7
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSListFragment<P extends ITSListPresenter<T>, T> extends TSFragment<P> implements BGARefreshLayout.BGARefreshLayoutDelegate, ITSListView<T, P> {
    public static final int DEFAULT_PAGE_MAX_ID = 0;// 默认初始化列表 id

    private static final float DEFAULT_LIST_ITEM_SPACING = 1f;

    protected CommonAdapter<T> mAdapter;

    protected BGARefreshLayout mRefreshlayout;

    protected RecyclerView mRvList;

    protected EmptyView mEmptyView;

    // 当前数据加载状态
    protected int mEmptyState = EmptyView.STATE_DEFAULT;

    protected int mMaxId = DEFAULT_PAGE_MAX_ID; // 纪录当前列表 item id 最大值，用于分页

    protected boolean mIsGetNetData = false; //  是否请求了网络数据


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mRefreshlayout = (BGARefreshLayout) rootView.findViewById(R.id.refreshlayout);
        mRvList = (RecyclerView) rootView.findViewById(R.id.rv_list);
        mEmptyView = new EmptyView(getContext());
        mEmptyView.setNeedTextTip(false);
        mEmptyView.setNeedClickLoadState(false);
        RxView.clicks(mEmptyView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mRefreshlayout.beginRefreshing();
                    }
                });
        mRefreshlayout.setDelegate(this);
        mRvList.setLayoutManager(getLayoutManager());
        mRvList.addItemDecoration(getItemDecoration());//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvList.setHasFixedSize(true);
        mRvList.setItemAnimator(new DefaultItemAnimator());//设置动画
        mAdapter = getAdapter();
        mRvList.setAdapter(mAdapter);
        mRefreshlayout.setRefreshViewHolder(new TSPRefreshViewHolder(getActivity(), isLoadingMoreEnable()));
        EmptyWrapper mEmptyWrapper = new EmptyWrapper(mAdapter);
        mEmptyWrapper.setEmptyView(mEmptyView);
        mRvList.setAdapter(mEmptyWrapper);
        mRefreshlayout.setIsShowLoadingMoreView(getIsShowLoadingMore());
        mRefreshlayout.setPullDownRefreshEnable(getPullDownRefreshEnable());
    }


    @Override
    protected void initData() {
        onCacheResponseSuccess(mPresenter.requestCacheData(mMaxId, false),false); // 获取缓存数据
    }

    /**
     * 设置 LayoutManager 区分列表样式
     *
     * @return
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    /**
     * 设置 item 间距数值
     *
     * @return 上下间距
     */
    protected float getItemDecorationSpacing() {
        return DEFAULT_LIST_ITEM_SPACING;
    }

    /**
     * An ItemDecoration allows the application to add a special drawing and layout offset
     * to specific item views from the adapter's data set. This can be useful for drawing dividers
     * between items, highlights, visual grouping boundaries and more.
     * <p>
     * <p>All ItemDecorations are drawn in the order they were added, before the item
     * views (in {@link RecyclerView.ItemDecoration#onDraw(Canvas, RecyclerView, RecyclerView.State) onDraw()}
     * and after the items (in {@link RecyclerView.ItemDecoration#onDrawOver(Canvas, RecyclerView,
     * RecyclerView.State)}.</p>
     */
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new LinearDecoration(0, ConvertUtils.dp2px(getContext(), getItemDecorationSpacing()), 0, 0);
    }

    protected boolean isLoadingMoreEnable() {
        return true;
    }

    protected boolean getIsShowLoadingMore() {
        return true;
    }

    protected boolean getPullDownRefreshEnable() {
        return true;
    }

    /**
     * 适配器
     *
     * @return
     */
    protected abstract CommonAdapter<T> getAdapter();

    /**
     * 插入或者更新缓存
     */
    protected abstract boolean insertOrUpdateData(@NotNull List<T> data);

    /**
     * 刷新数据
     */
    public void refreshData() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新
     *
     * @param refreshLayout 刷新控件
     */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mMaxId = DEFAULT_PAGE_MAX_ID;
        mPresenter.requestNetData(mMaxId, false);
    }

    /**
     * 加载更多
     *
     * @param refreshLayout 刷新控件
     * @return
     */
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (mIsGetNetData) { // 如果没有获取过网络数据，加载更多就是获取本地数据，如果加载了网络数据了，加载更多就是获取网络数据
            onCacheResponseSuccess(mPresenter.requestCacheData(mMaxId, true),true);
        } else {
            mPresenter.requestNetData(mMaxId, true);
        }
        return true;
    }

    /**
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    @Override
    public void onNetResponseSuccess(@NotNull List<T> data, boolean isLoadMore) {
        mIsGetNetData = true;
        handleRefreshState(isLoadMore);
        handleReceiveData(data, isLoadMore);
    }

    /**
     * 处理获取到的缓存数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    @Override
    public void onCacheResponseSuccess(@NotNull List<T> data, boolean isLoadMore) {
        handleReceiveData(data, isLoadMore);
    }

    /**
     * @param throwable  具体错误信息
     * @param isLoadMore 加载状态
     */
    @Override
    public void onResponseError(Throwable throwable, boolean isLoadMore) {
        handleRefreshState(isLoadMore);
        if (!isLoadMore) { // 刷新
            mEmptyView.setErrorType(EmptyView.STATE_NETWORK_ERROR);
        } else { // 加载更多
            ToastUtils.showToast("加载错误");

        }
    }

    /**
     * 处理服务器或者缓存中拿到的数据
     *
     * @param data       返回的数据
     * @param isLoadMore 是否是加载更多
     */
    private void handleReceiveData(@NotNull List<T> data, boolean isLoadMore) {
        if (!isLoadMore) { // 刷新
            mAdapter.clear();
            if (data.size() != 0) {
                // 更新缓存
                insertOrUpdateData(data);
                // 内存处理数据
                mAdapter.addAllData(data);
                mRefreshlayout.setIsShowLoadingMoreView(true);
//                mMaxId=data.get(data.size()-1).getmaxid;
            } else {
                mRefreshlayout.setIsShowLoadingMoreView(false);
            }
            refreshData();
        } else { // 加载更多
            if (data.size() != 0) {
                // 更新缓存
                insertOrUpdateData(data);
                // 内存处理数据
                mAdapter.addAllData(data);
                refreshData();
                //                mMaxId=data.get(data.size()-1).getmaxid;
            } else {
                ToastUtils.showToast("没有更多数据了");
                mRefreshlayout.setIsShowLoadingMoreView(false);
            }
        }
    }

    /**
     * 处理关闭加载、刷新状态
     *
     * @param isLoadMore
     */
    private void handleRefreshState(boolean isLoadMore) {
        if (isLoadMore) {
            mRefreshlayout.endLoadingMore();
        } else {
            mRefreshlayout.endRefreshing();
        }
    }
}
