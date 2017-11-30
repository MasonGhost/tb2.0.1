package com.zhiyicx.zhibolibrary.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerLiveItemComponent;
import com.zhiyicx.zhibolibrary.di.module.LiveItemModule;
import com.zhiyicx.zhibolibrary.presenter.LiveItemPresenter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.SpaceItemGridDecoration;
import com.zhiyicx.zhibolibrary.ui.adapter.SpaceItemLinearBaseDecoration;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.components.refresh.NormalRefreshViewHolder;
import com.zhiyicx.zhibolibrary.ui.components.refresh.RefreshLayout;
import com.zhiyicx.zhibolibrary.ui.view.LiveItemView;
import com.zhiyicx.zhibolibrary.util.DeviceUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhiyicx on 2016/3/30.
 */
public abstract class ZBLLiveItemFragment extends ZBLBaseFragment implements LiveItemView, RefreshLayout.BGARefreshLayoutDelegate {

    public static final String TYPE_FOLLOW = "follow";//关注
    public static final String TYPE_ONLINE = "online";//在线
    public static final String TYPE_AIRTIME = "airtime";//最新
    public static final String TYPE_VALL = "all";//全部
    public static final String TYPE_VIDEO = "video";//回放
    public static final String TYPE_OWN = "own";//自己
    @Inject
    LiveItemPresenter mPresenter;

    protected View mRootView;

    protected RefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    protected TextView mPlaceHolder;

    private GridLayoutManager mLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean mFirst;
    private ProgressDialog mLoading;
    private boolean isFilter;
    public Map<String, Object> mFilterValues = new HashMap<>();
    private String mUid;
    private String mUsid;
    protected boolean isNeedShowUserInfo = true;//列表中是否需要显示用户头像名字等信息;

    @Override
    protected View initView() {
        mRootView = UiUtils.inflate(getLayout());
        mRefreshLayout = (RefreshLayout) mRootView.findViewById(R.id.srl_live);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_live);
        mPlaceHolder = (TextView) mRootView.findViewById(R.id.iv_live_item_placeholder);
        return mRootView;
    }

    @Override
    protected void initData() {
        DaggerLiveItemComponent
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .liveItemModule(new LiveItemModule(this))
                .build()
                .inject(this);
        initRecycleView();
        initDialog();
    }

    private void initDialog() {
        mLoading = new ProgressDialog(getActivity());
        mLoading.setMessage(UiUtils.getString(R.string.str_loading));
        mLoading.setCanceledOnTouchOutside(false);
    }

    private void initRecycleView() {
        mRefreshLayout.setDelegate(this);
        if (getOrder().equals(TYPE_FOLLOW)) {

            //创建默认的线性LayoutManager
            mLinearLayoutManager = new LinearLayoutManager(UiUtils.getContext());
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.addItemDecoration(new SpaceItemLinearBaseDecoration(0, AutoUtils.getPercentWidthSize(2), 0, 0));//设置Item的间隔
        } else {

            //创建默认的线性LayoutManager
            mLayoutManager = new GridLayoutManager(UiUtils.getContext(), 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new SpaceItemGridDecoration((int) DeviceUtils.dpToPixel(getActivity(), 5)));//设置Item的间隔
        }

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (getOrder().equals(TYPE_FOLLOW)) {
                    lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                } else {
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mRefreshLayout != null && mRefreshLayout.getCurrentRefreshStatus() == RefreshLayout.RefreshStatus.REFRESHING) {
                    return;
                }
                if (getOrder().equals(TYPE_FOLLOW)) {
                    MoreLinearAdapter adapter = (MoreLinearAdapter) recyclerView.getAdapter();
                    if (adapter == null) {
                        return;
                    }
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItem + 1 == adapter.getItemCount()
                            && adapter.isShowFooter()) {
                        //加载更多
                        mPresenter.getList(true);
                    }
                } else {
                    MoreAdapter adapter = (MoreAdapter) recyclerView.getAdapter();
                    if (adapter == null) {
                        return;
                    }
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItem + 1 == adapter.getItemCount()
                            && adapter.isShowFooter()) {
                        //加载更多
                        mPresenter.getList(true);
                    }
                }
            }
        });
        mRefreshLayout.setRefreshViewHolder(new NormalRefreshViewHolder(getActivity().getApplicationContext(), false));
    }

    @Override
    public void setData() {
        if (!mFirst && mRefreshLayout != null) {

            mRefreshLayout.beginRefreshing();

        }
    }

    /**
     * 刷新列表
     */
    public void RefreshList() {
        mPresenter.getList(false);
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
    public void setAdapter(MoreAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 用于关注
     *
     * @param adapter
     */
    @Override
    public void setMoreLineAdapter(MoreLinearAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.animate_null);

    }

///
//    @Override
//    public void showNetBadPH() {
//        mPlaceHolder.setVisibility(View.VISIBLE);
//        mPlaceHolder.setBackgroundResource(R.mipmap.wifi_pic_default);
//    }


    @Override
    public void showNotFollowPH() {
        mPlaceHolder.setVisibility(View.VISIBLE);
        mPlaceHolder.setText(getResources().getString(R.string.no_attentions));
    }

    @Override
    public void shwoNothingPH() {
        mPlaceHolder.setVisibility(View.VISIBLE);
        mPlaceHolder.setText(getResources().getString(R.string.nothing_find));
    }

    @Override
    public void showFilterNothingPH() {
        mPlaceHolder.setVisibility(View.VISIBLE);
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
    public void killMyself() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();//销毁
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
    }

    /**
     * 上拉刷新
     */

    @Override
    public void onBGARefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
        mPresenter.getList(false);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
        return false;
    }

    /**
     * 子类实现，区分列表的类型
     *
     * @return
     */
    @Override
    public abstract String getOrder();

    public abstract int getLayout();

    /**
     * 回放页面的order
     *
     * @return
     */
    @Override
    public String getVideoOreder() {
        return "";
    }

    /**
     * 是否筛选
     *
     * @return
     */
    @Override
    public boolean isFilter() {
        return isFilter;
    }

    @Override
    public void setIsFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }

    @Override
    public boolean isNeedShowUserInfo() {
        return isNeedShowUserInfo;
    }

    /**
     * 筛选要的参数
     *
     * @param map
     */
    public void setFilterValue(Map<String, Object> map) {
        this.mFilterValues = map;
    }

    @Override
    public Map<String, Object> getFilterValue() {
        return mFilterValues;
    }

    @Override
    public String getUid() {
        return this.mUid;
    }

    @Override
    public void setUid(String uid) {
        this.mUid = uid;
    }

    @Override
    public String getUsid() {
        return mUsid;
    }

    public void setUsid(String usid) {
        this.mUsid = usid;
    }

}
