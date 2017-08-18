package com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @Describe 问答搜索列表页
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
public class QASearchListFragment extends TSListFragment<QASearchListContract.Presenter, QAListInfoBean> implements QASearchListContract.View {

    @BindView(R.id.rv_search_history)
    RecyclerView mRvSearchHistory;

    @Inject
    QASearchListPresenter mQASearchListPresenter;

    private MultiItemTypeAdapter mHsitoryAdapter;
    private List<QAListInfoBean> mHistoryData=new ArrayList<>();


    public static QASearchListFragment newInstance(Bundle bundle) {
        QASearchListFragment followFansListFragment = new QASearchListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_qa_search_list;
    }

    @Override
    protected CommonAdapter<QAListInfoBean> getAdapter() {
        return new QASearchListAdapter(getContext(), R.layout.item_qa_search, mListDatas);
    }

    @Override
    protected void initView(View rootView) {
        DaggerQASearchListPresenterComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .qASearchListPresenterModule(new QASearchListPresenterModule(QASearchListFragment.this))
                .build().inject(this);

        super.initView(rootView);

        initHistoryView();

    }

    private void initHistoryView() {
        mRvSearchHistory.setLayoutManager(getLayoutManager());
        mRvSearchHistory.addItemDecoration(getItemDecoration());//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvSearchHistory.setHasFixedSize(sethasFixedSize());
        mRvSearchHistory.setItemAnimator(new DefaultItemAnimator());//设置动画
        mHsitoryAdapter = getHistoryAdapter();
        mRvSearchHistory.setAdapter(mHsitoryAdapter);
    }


    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    public MultiItemTypeAdapter getHistoryAdapter() {
        return new QASearchListAdapter(getContext(), R.layout.item_qa_search_history_list, mHistoryData);
    }

    @Override
    public String getSearchInput() {
        return "我";
    }
}
