package com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.search.list.IHistoryCententClickListener;
import com.zhiyicx.thinksnsplus.modules.q_a.search.list.ISearchListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa.QASearchListPresenter.DEFAULT_FIRST_SHOW_HISTORY_SIZE;

/**
 * @Describe 问答搜索列表页
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
public class QASearchListFragment extends TSListFragment<QASearchListContract.Presenter, QAListInfoBean>
        implements QASearchListContract.View, ISearchListener {

    @BindView(R.id.rv_search_history)
    RecyclerView mRvSearchHistory;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.bt_do)
    Button mBtDo;

    @BindView(R.id.ll_empty)
    LinearLayout mLlEmpty;

    @Inject
    QASearchListPresenter mQASearchListPresenter;

    private String mSearchContent = "";
    private MultiItemTypeAdapter mHsitoryAdapter;
    private List<QASearchHistoryBean> mHistoryData = new ArrayList<>();


    private IHistoryCententClickListener mIHistoryCententClickListener;


    public static QASearchListFragment newInstance(Bundle bundle) {
        QASearchListFragment followFansListFragment = new QASearchListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IHistoryCententClickListener) {
            this.mIHistoryCententClickListener = (IHistoryCententClickListener) activity;
        }

    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return false;
    }

    @Override
    protected Long getMaxId(@NotNull List<QAListInfoBean> data) {
        return (long) mListDatas.size();
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
        mTvTip.setText(getString(R.string.not_find_qa_to_publish));
        mBtDo.setText(getString(R.string.to_publish_qa));
        mHistoryData.addAll(mPresenter.getFirstShowHistory());
        if (mHistoryData.size() >= DEFAULT_FIRST_SHOW_HISTORY_SIZE) {
            mHistoryData.add(new QASearchHistoryBean(getString(R.string.show_all_history), QASearchHistoryBean.TYPE_DEFAULT));
        }
        mRvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvSearchHistory.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), getItemDecorationSpacing()), 0, 0));//设置Item的间隔
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
        mHsitoryAdapter = new MultiItemTypeAdapter<>(getContext(), mHistoryData);
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
                            if (mIHistoryCententClickListener != null) {
                                onEditChanged(qaSearchHistoryBean.getContent());
                                mIHistoryCententClickListener.onContentClick(qaSearchHistoryBean.getContent());
                            }

                        });
                RxView.clicks(holder.getView(R.id.iv_delete))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> {
                            mPresenter.deleteSearchHistory(mHistoryData.get(position));
                            mHistoryData.remove(position);
                            mHsitoryAdapter.notifyItemRemoved(position);
                            mHsitoryAdapter.notifyDataSetChanged();

                        });
            }
        });
        mHsitoryAdapter.addItemViewDelegate(new ItemViewDelegate<QASearchHistoryBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_qa_search_history_cotrol;
            }

            @Override
            public boolean isForViewType(QASearchHistoryBean item, int position) {
                return item.getType() == QASearchHistoryBean.TYPE_DEFAULT;

            }

            @Override
            public void convert(ViewHolder holder, QASearchHistoryBean o, QASearchHistoryBean lastT, int position, int itemCounts) {
                holder.setText(R.id.tv_content, o.getContent());
                RxView.clicks(holder.getView(R.id.tv_content))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> {
                            if (o.getContent().equals(getString(R.string.show_all_history))) { // 显示所有历史
                                mHistoryData.clear();
                                mHistoryData.addAll(mPresenter.getAllSearchHistory());
                                mHistoryData.add(new QASearchHistoryBean(getString(R.string.clear_all_history), QASearchHistoryBean.TYPE_DEFAULT));
                                mHsitoryAdapter.notifyDataSetChanged();

                            } else { // 清空历史
                                mHistoryData.clear();
                                mPresenter.cleaerAllSearchHistory();
                                mHsitoryAdapter.notifyDataSetChanged();

                            }
                        });
            }
        });
    }

    @Override
    public String getSearchInput() {
        return mSearchContent;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        checkEmptyView();
    }

    @Override
    public void onCacheResponseSuccess(List<QAListInfoBean> data, boolean isLoadMore) {
    }

    @Override
    public void onResponseError(Throwable throwable, boolean isLoadMore) {
        super.onResponseError(throwable, isLoadMore);
        checkEmptyView();
    }

    private void checkEmptyView() {
        if (mListDatas.isEmpty()) {
            mLlEmpty.setVisibility(View.VISIBLE);
        } else {
            mLlEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEditChanged(String str) {
        if (mSearchContent.equals(str)) {
            return;
        }
        mSearchContent = str;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        // 请求网络数据，就隐藏历史
        mRvSearchHistory.setVisibility(View.GONE);


        if (mRefreshlayout.isRefreshing()) {
            onRefresh(mRefreshlayout);
        } else {
            mRefreshlayout.autoRefresh();
        }

    }


    @OnClick(R.id.bt_do)
    public void onViewClicked() {
        // 发布问答
        startActivity(new Intent(getActivity(), PublishQuestionActivity.class));
    }
}
