package com.zhiyicx.zhibolibrary.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerGoldHistoryComponent;
import com.zhiyicx.zhibolibrary.di.module.GoldHistoryModule;
import com.zhiyicx.zhibolibrary.presenter.GoldHistoryPresenter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.SpaceItemLinearBaseDecoration;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.components.refresh.NormalRefreshViewHolder;
import com.zhiyicx.zhibolibrary.ui.components.refresh.RefreshLayout;
import com.zhiyicx.zhibolibrary.ui.view.GoldHistoryView;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.utils.AutoUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by jess on 16/4/26.
 */
public class ZBLGoldHistoryActivity extends ZBLBaseActivity implements GoldHistoryView, RefreshLayout.BGARefreshLayoutDelegate,View.OnClickListener {
    @Inject
    GoldHistoryPresenter mPresenter;
    RefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    TextView mPlaceHolder;

    private String mType="";
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void initView() {
        setContentView(R.layout.zb_activity_gold_history);
         mRefreshLayout = (RefreshLayout) findViewById(R.id.srl_gold_history);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_gold_history);
        mPlaceHolder = (TextView) findViewById(R.id.iv_history_placeholder);
        findViewById(R.id.rl_gold_history_back).setOnClickListener(this);

    }

    @Override
    protected void initData() {
        DaggerGoldHistoryComponent
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .goldHistoryModule(new GoldHistoryModule(this))
                .build()
                .inject(this);
        mType = getIntent().getStringExtra("type");
        initRecycleView();
        mRefreshLayout.beginRefreshing();//默认刷新一次
    }

    @Override
    public void onBackPressed() {
        killMyself();
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.rl_gold_history_back) {
                killMyself();
        }
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
                if (mRefreshLayout!=null&&mRefreshLayout.getCurrentRefreshStatus() == RefreshLayout.RefreshStatus.REFRESHING)
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
        mRefreshLayout.setRefreshViewHolder(new NormalRefreshViewHolder(this, false));
    }


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        mRefreshLayout.endRefreshing();
    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        super.onBackPressed();
    }

    @Override
    public void setAdapter(MoreLinearAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public String getType() {
        if(mType==null)
            return "";
        return mType;
    }

    @Override
    public void showPlaceHolder() {
        mPlaceHolder.setVisibility(View.VISIBLE);
        mPlaceHolder.setText(getResources().getString(R.string.nothing_record_find));
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
        return false;
    }
}
