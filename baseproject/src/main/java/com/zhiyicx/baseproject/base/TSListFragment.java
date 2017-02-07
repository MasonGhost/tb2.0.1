package com.zhiyicx.baseproject.base;


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
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.TSPRefreshViewHolder;


/**
 * @Describe 基础列表类
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSListFragment<P extends IBasePresenter, T> extends TSFragment<P> implements BGARefreshLayout.BGARefreshLayoutDelegate {

    protected List<T> mDatas;

    protected CommonAdapter<T> mAdapter;


    protected BGARefreshLayout mRefreshlayout;

    protected RecyclerView mRvList;

    protected EmptyView mEmptyView;

    //当前数据加载状态
    protected int mEmptyState =EmptyView.STATE_DEFAULT;

    @Override
    protected void initView(View rootView) {
        mRefreshlayout = (BGARefreshLayout) rootView.findViewById(R.id.refreshlayout);
        mRvList = (RecyclerView) rootView.findViewById(R.id.rv_list);
        mEmptyView = (EmptyView) rootView.findViewById(R.id.emptyview);


        mRefreshlayout.setDelegate(this);
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvList.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), LIST_ITEM_SPACING), 0, 0));//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvList.setHasFixedSize(true);
        mRvList.setItemAnimator(new DefaultItemAnimator());//设置动画
        mRvList.setAdapter(new CommonAdapter<MessageItemBean>(getActivity(), R.layout.item_message_like_list, mMessageItemBeen) {
            @Override
            protected void convert(ViewHolder holder, MessageItemBean messageItemBean, int position) {
                setItemData(holder, messageItemBean, position);
            }

        });
        mRefreshlayout.setRefreshViewHolder(new TSPRefreshViewHolder(getActivity(), true));
        EmptyWrapper mEmptyWrapper = new EmptyWrapper(mAdapter);
        mEmptyWrapper.setEmptyView(mEmptyView);
        mRvList.setAdapter(mEmptyWrapper);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
