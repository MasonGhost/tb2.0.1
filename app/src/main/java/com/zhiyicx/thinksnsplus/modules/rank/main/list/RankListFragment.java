package com.zhiyicx.thinksnsplus.modules.rank.main.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhiyicx.thinksnsplus.modules.rank.adapter.RankIndexAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class RankListFragment extends TSListFragment<RankListContract.Presenter, RankIndexBean> implements RankListContract.View{

    public static final String BUNDLE_RANK_TYPE = "bundle_rank_type";

    private RankIndexBean mRankIndexBean;

    @Inject
    RankListPresenter rankListPresenter;

    public RankListFragment instance(Bundle bundle){
        RankListFragment fragment = new RankListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerRankListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .rankListPresenterModule(new RankListPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected void initData() {
        super.initData();
        if (mRankIndexBean == null){
            mRankIndexBean = (RankIndexBean) getArguments().getSerializable(BUNDLE_RANK_TYPE);
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        RankIndexAdapter adapter = new RankIndexAdapter(getContext(), mListDatas,mPresenter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean isUseTouristLoadLimit() {
        return false;
    }

    @Override
    public String getCategory() {
        if (mRankIndexBean == null){
            mRankIndexBean = (RankIndexBean) getArguments().getSerializable(BUNDLE_RANK_TYPE);
        }
        return mRankIndexBean == null ? "" : mRankIndexBean.getCategory();
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }
}
