package com.zhiyicx.baseproject.base;


import android.view.View;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.mvp.i.IBasePresenter;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * @Describe 基础列表类
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSListFragment<P extends IBasePresenter,T> extends TSFragment<P> implements BGARefreshLayout.BGARefreshLayoutDelegate{

    protected List<T> mDatas;

    @Override
    protected void initView(View rootView) {

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
