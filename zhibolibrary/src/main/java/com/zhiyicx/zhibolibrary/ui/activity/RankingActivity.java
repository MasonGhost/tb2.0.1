package com.zhiyicx.zhibolibrary.ui.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerRankingComponent;
import com.zhiyicx.zhibolibrary.di.module.RankingModule;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.presenter.RankingPresenter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.SpaceItemLinearBaseDecoration;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.components.refresh.MeiTuanRefreshViewHolder;
import com.zhiyicx.zhibolibrary.ui.components.refresh.RefreshLayout;
import com.zhiyicx.zhibolibrary.ui.view.RankingView;
import com.zhiyicx.zhibolibrary.util.Anim;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by jess on 16/4/23.
 */
public class RankingActivity extends ZBLBaseActivity implements RankingView, RefreshLayout.BGARefreshLayoutDelegate, View.OnClickListener {
    protected RelativeLayout rlRankingBack;
    protected RecyclerView mRecyclerView;
    protected RefreshLayout mRefreshLayout;
    protected ImageView mPlaceHolder;


    @Inject
    RankingPresenter mPresenter;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;

    @Override
    protected void initView() {
        setContentView(R.layout.zb_activity_ranking);
        rlRankingBack = (RelativeLayout) findViewById(R.id.rl_ranking_back);
        rlRankingBack.setOnClickListener(RankingActivity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_ranking);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.srl_ranking);
        mPlaceHolder = (ImageView) findViewById(R.id.iv_ranking_placeholder);
    }

    @Override
    protected void initData() {
        DaggerRankingComponent
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .rankingModule(new RankingModule(this))
                .build()
                .inject(this);
        initRecycleView();
//        mPresenter.getList(false);//初始化更新
        mRefreshLayout.beginRefreshing();
    }

    private void initRecycleView() {
        mRefreshLayout.setDelegate(this);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(UiUtils.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        mRecyclerView.addItemDecoration(new SpaceItemLinearBaseDecoration(AutoUtils.getPercentWidthSize(2), 0, 0, 0));//设置Item的间隔
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


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
                MoreLinearAdapter adapter = (MoreLinearAdapter) mRecyclerView.getAdapter();
                if (adapter == null) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()
                        && adapter.isShowFooter()) {
                    //加载更多
                    Log.w(TAG, "loading more data");
                    mPresenter.getList(true);
                }
            }
        });

        mRefreshLayout.setRefreshViewHolder(new MeiTuanRefreshViewHolder(this, false));

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        startActivity(intent);
        Anim.in(this);
    }

    @Override
    public void killMyself() {
        super.onBackPressed();
        EventBus.getDefault().post(true, "my_fragment_get_user_count");
    }

    @Override
    public void onBackPressed() {
        killMyself();
    }


    @Override
    public void setAdapter(MoreLinearAdapter adapter) {


        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public String getOrder() {
        return UserService.RANKING_ORDER_FANS;
    }

    @Override
    public void showRefreshing() {

    }

    @Override
    public void hideRefreshing() {
        mRefreshLayout.endRefreshing();

    }

    @Override
    public void hideLoadMore() {
        mRefreshLayout.endLoadingMore();
    }

    @Override
    public void showPlaceHolder() {
        mPlaceHolder.setVisibility(View.VISIBLE);
        mPlaceHolder.setImageResource(R.mipmap.ranklist_pic_default);
    }

    @Override
    public void showNetBadPH() {
        mPlaceHolder.setVisibility(View.VISIBLE);
        mPlaceHolder.setImageResource(R.mipmap.wifi_pic_default);
    }

    @Override
    public void hidePlaceHolder() {
        mPlaceHolder.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
        mPresenter.getList(false);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
        mPresenter.getList(true);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_ranking_back) {
            killMyself();
        }
    }
}
