package com.zhiyicx.zhibolibrary.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerSearchTabComponent;
import com.zhiyicx.zhibolibrary.di.module.SearchTabModule;
import com.zhiyicx.zhibolibrary.presenter.SearchTabPresenter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.SpaceItemLinearBaseDecoration;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.components.refresh.MeiTuanRefreshViewHolder;
import com.zhiyicx.zhibolibrary.ui.components.refresh.NormalRefreshViewHolder;
import com.zhiyicx.zhibolibrary.ui.components.refresh.RefreshLayout;
import com.zhiyicx.zhibolibrary.ui.view.SearchTabView;
import com.zhiyicx.zhibolibrary.util.Anim;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.utils.AutoUtils;

import javax.inject.Inject;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class ZBLSearchTabFragement extends ZBLBaseFragment implements SearchTabView, RefreshLayout.BGARefreshLayoutDelegate {

    @Inject
    SearchTabPresenter mPresenter;

    RefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;

    TextView mPlaceHolder;

    private View mRootView;
    private String type;
    private LinearLayoutManager mLayoutManager;
    private String mKeyword = "";//推荐时传""
    private ProgressDialog mLoading;
    public final static String DEFAULT_KEYWORD = "default";


    public static ZBLSearchTabFragement newInstance(String type) {
        Bundle args = new Bundle();
        ZBLSearchTabFragement fragment = new ZBLSearchTabFragement();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView() {
        this.type = getArguments().getString("type");
        mRootView = UiUtils.inflate(R.layout.zb_fragment_search_tab);
        mRefreshLayout = (RefreshLayout) mRootView.findViewById(R.id.srl_search_tab);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_search_tab);
        mPlaceHolder = (TextView) mRootView.findViewById(R.id.iv_search_tab_placeholder);

        return mRootView;
    }

    @Override
    protected void initData() {
        initRecycleView();
        DaggerSearchTabComponent//注入
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .searchTabModule(new SearchTabModule(this))
                .build()
                .inject(this);
        initDialog();
        mRefreshLayout.beginRefreshing();
    }

    private void initDialog() {
        mLoading = new ProgressDialog(getActivity());
        mLoading.setMessage(UiUtils.getString(R.string.str_loading));
        mLoading.setCanceledOnTouchOutside(false);
    }

    private void initRecycleView() {
        mRefreshLayout.setDelegate(this);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(UiUtils.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        mRecyclerView.addItemDecoration(new SpaceItemLinearBaseDecoration(0,AutoUtils.getPercentWidthSize(2), 0, 0));//设置Item的间隔
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            private int lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mRefreshLayout != null && mRefreshLayout.getCurrentRefreshStatus() == RefreshLayout.RefreshStatus.REFRESHING)
                    return;
                RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                if (adapter == null) {
                    return;
                }
                MoreLinearAdapter moreAdapter = (MoreLinearAdapter) adapter;
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == moreAdapter.getItemCount()
                        && moreAdapter.isShowFooter()) {
                    //加载更多
                    Log.w(TAG, "loading more data");
                    mPresenter.search(mKeyword, true);
                }
            }
        });

        //初始化freshLyout

//        mRefreshLayout.setRefreshViewHolder(new MeiTuanRefreshViewHolder(this.getActivity(), false));
        mRefreshLayout.setRefreshViewHolder(new NormalRefreshViewHolder(this.getActivity(), false));
//        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);

    }

    @Override
    public void setData(Object data) {
        if (data instanceof Boolean) {//判断是否开启刷新,又applayout控制
            mRefreshLayout.setPullDownRefreshEnable((Boolean) data);//让下拉刷新不可用
            return;
        }
        if (mKeyword == null || !data.equals(mKeyword)) {//第一查询或查询关键字变更才执行查询
            mRefreshLayout.setPullDownRefreshEnable(true);//让下拉刷新不可用
            mPresenter.search((String) data, false);//查询
        }
        mKeyword = (String) data;
    }

    @Override
    public void showLoading() {
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        mLoading.dismiss();
    }

    @Override
    public void showRefreshing() {

    }

    @Override
    public void hideRefreshing() {
        mRefreshLayout.endRefreshing();
    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void setAdapter(MoreLinearAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void launchActivity(Intent intent) {
        this.startActivity(intent);
        Anim.in(getActivity());
    }

    @Override
    public void killMyself() {

    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void showPlaceHolder() {
        mPlaceHolder.setVisibility(View.VISIBLE);
//        mPlaceHolder.setImageResource(R.mipmap.search_pic_default);
        mPlaceHolder.setText(getResources().getString(R.string.no_search_result));
    }

    @Override
    public void showNetBadPH() {
        mPlaceHolder.setVisibility(View.VISIBLE);
        mPlaceHolder.setText(getResources().getString(R.string.no_net));
    }


    @Override
    public void hidePlaceHolder() {
        mPlaceHolder.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
        if (mKeyword != null) {
            mPresenter.search(mKeyword, false);//查询
        }
        else {
            showMessage("请先输入关键字!");
            hideRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
        return false;
    }
}
