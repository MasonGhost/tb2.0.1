package com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.modules.q_a.search.list.ISearchListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 问答搜索列表页
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
public class QASearchListFragment extends TSListFragment<QASearchListContract.Presenter, QAListInfoBean> implements QASearchListContract.View, ISearchListener {

    @BindView(R.id.rv_search_history)
    RecyclerView mRvSearchHistory;

    @Inject
    QASearchListPresenter mQASearchListPresenter;

    private String mSearchContent = "";
    private MultiItemTypeAdapter mHsitoryAdapter;
    private List<QASearchHistoryBean> mHistoryData = new ArrayList<>();


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

        mHistoryData = mPresenter.getFirstShowHistory();
        mRvSearchHistory.setLayoutManager(getLayoutManager());
        mRvSearchHistory.addItemDecoration(getItemDecoration());//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvSearchHistory.setHasFixedSize(sethasFixedSize());
        mRvSearchHistory.setItemAnimator(new DefaultItemAnimator());//设置动画
        getHistoryAdapter();
        mRvSearchHistory.setAdapter(mHsitoryAdapter);
        refreshHistory();
    }

    private void refreshHistory() {
        mHsitoryAdapter.notifyDataSetChanged();
        if (mHistoryData.isEmpty()) {
            mRvSearchHistory.setVisibility(View.GONE);
        } else {
            mRvSearchHistory.setVisibility(View.VISIBLE);
        }
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

    public void getHistoryAdapter() {
        mHsitoryAdapter = new MultiItemTypeAdapter(getContext(), mListDatas);
        mHsitoryAdapter.addItemViewDelegate(new ItemViewDelegate<QASearchHistoryBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_qa_search_history_list;
            }

            @Override
            public boolean isForViewType(QASearchHistoryBean item, int position) {
                return item.getType() != QASearchHistoryBean.TYPE_DEFAULT;
            }

            @Override
            public void convert(ViewHolder holder, QASearchHistoryBean qaSearchHistoryBean, QASearchHistoryBean lastT, int position, int itemCounts) {
                holder.setText(R.id.tv_content, qaSearchHistoryBean.getContent());
                RxView.clicks(holder.getView(R.id.tv_content))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> {
                            // TODO: 2017/8/18 增加搜索输入

                        });
                RxView.clicks(holder.getView(R.id.iv_delete))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> {
                            // TODO: 2017/8/18 删除历史
                            mListDatas.remove(position);
                            mHsitoryAdapter.notifyItemRemoved(position);
                        });
            }
        });
    }

    @Override
    public String getSearchInput() {
        return mSearchContent;
    }

    @Override
    public void onEditChanged(String str) {
        mSearchContent = str;
        if (mRefreshlayout.isRefreshing()) {
            onRefresh();
        } else {
            mRefreshlayout.setRefreshing(true);
        }

    }
}
