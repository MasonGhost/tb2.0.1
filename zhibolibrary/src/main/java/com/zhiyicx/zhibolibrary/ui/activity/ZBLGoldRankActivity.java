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
import com.zhiyicx.zhibolibrary.di.component.DaggerGoldRankComponent;
import com.zhiyicx.zhibolibrary.di.module.GoldRankModule;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.presenter.GoldRankPresenter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.SpaceItemLinearBaseDecoration;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.components.refresh.MeiTuanRefreshViewHolder;
import com.zhiyicx.zhibolibrary.ui.components.refresh.NormalRefreshViewHolder;
import com.zhiyicx.zhibolibrary.ui.components.refresh.RefreshLayout;
import com.zhiyicx.zhibolibrary.ui.view.GoldRankView;
import com.zhiyicx.zhibolibrary.util.Anim;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by jungle on 16/5/24.
 */
public class ZBLGoldRankActivity extends ZBLBaseActivity implements GoldRankView, RefreshLayout.BGARefreshLayoutDelegate, View.OnClickListener {
    public static final String KEY_MUID = "mUsid";
    protected RelativeLayout rlRankGoldBack;
    protected RecyclerView mRecyclerView;
    protected RefreshLayout mRefreshLayout;
    protected ImageView mPlaceHolder;

    @Inject
    GoldRankPresenter mPresenter;
    private LinearLayoutManager mLayoutManager;

    private String mUsid = "";//当前选择的主播的赠送礼物排行榜

    @Override
    protected void initView() {
        setContentView(R.layout.zb_activity_rank_gold);
        rlRankGoldBack = (RelativeLayout) findViewById(R.id.rl_rank_gold_back);
        rlRankGoldBack.setOnClickListener(ZBLGoldRankActivity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_rank_gold);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.srl_rank_gold);
        mPlaceHolder = (ImageView) findViewById(R.id.iv_gold_ranking_placeholder);
    }

    @Override
    protected void initData() {
        DaggerGoldRankComponent
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .goldRankModule(new GoldRankModule(this))
                .build()
                .inject(this);
        if (getIntent().getExtras() != null) {
            mUsid = getIntent().getExtras().getString(KEY_MUID);
        }
        initRecycleView();
        mRefreshLayout.beginRefreshing();//默认刷新一次
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
                MoreLinearAdapter adapter = (MoreLinearAdapter) mRecyclerView.getAdapter();
                if (adapter == null) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()
                        && adapter.isShowFooter()) {
                    //加载更多
                    Log.w(TAG, "loading more data");
                    mPresenter.getList(true, mUsid);
                }
            }
        });
        mRefreshLayout.setRefreshViewHolder(new NormalRefreshViewHolder(this, false));
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
    public void showNetBadPH() {
        mPlaceHolder.setVisibility(View.VISIBLE);
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
        mPresenter.getList(false, mUsid);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
        return false;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_rank_gold_back) {
            killMyself();
        }
    }
}
