package com.zhiyicx.baseproject.base;


import android.graphics.Canvas;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.TSPRefreshViewHolder;


/**
 * @Describe 基础列表类
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSListFragment<P extends IBasePresenter, T> extends TSFragment<P> implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final float DEFAULT_LIST_ITEM_SPACING = 1f;

    protected CommonAdapter<T> mAdapter;

    protected BGARefreshLayout mRefreshlayout;

    protected RecyclerView mRvList;

    protected EmptyView mEmptyView;

    //当前数据加载状态
    protected int mEmptyState = EmptyView.STATE_DEFAULT;


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
        mEmptyView = (EmptyView) rootView.findViewById(R.id.emptyview);
        mRefreshlayout.setDelegate(this);
        mRvList.setLayoutManager(getLayoutManager());
        mRvList.addItemDecoration(getItemDecoration());//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvList.setHasFixedSize(true);
        mRvList.setItemAnimator(new DefaultItemAnimator());//设置动画
        mAdapter = getAdapter();
        mRvList.setAdapter(mAdapter);
        mRefreshlayout.setRefreshViewHolder(new TSPRefreshViewHolder(getActivity(), true));
        EmptyWrapper mEmptyWrapper = new EmptyWrapper(mAdapter);
        mEmptyWrapper.setEmptyView(mEmptyView);
        mRvList.setAdapter(mEmptyWrapper);
        mRefreshlayout.setIsShowLoadingMoreView(getIsShowLoadingMore());
        mRefreshlayout.setPullDownRefreshEnable(getPullDownRefreshEnable());
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

    protected boolean getIsShowLoadingMore() {
        return true;
    }

    protected boolean getPullDownRefreshEnable() {
        return true;
    }

    protected abstract CommonAdapter<T> getAdapter();


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        mAdapter.notifyDataSetChanged();
    }
}
