package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QATopicListFragment extends TSListFragment<QATopicListConstact.Presenter, QATopicBean>
        implements QATopicListConstact.View {

    @Inject
    QATopicListPresenter mQATopicListPresenter;

    public static final String BUNDLE_TOPIC_TYPE = "topic_type";

    public static QATopicListFragment newInstance(String params) {
        QATopicListFragment fragment = new QATopicListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_TOPIC_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onEmptyViewClick() {
        mRefreshlayout.setRefreshing(true);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
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
    protected RecyclerView.Adapter getAdapter() {
        return new CommonAdapter<QATopicBean>(getContext(), R.layout.item_channel_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, QATopicBean o, int position) {

            }
        };
    }

    @Override
    protected void initData() {
        DaggerQATopicListComponent
                .builder().appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .qATopiclistModule(new QATopiclistModule(this))
                .build().inject(this);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
//        super.initData();

    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
    }

    @Override
    protected List<QATopicBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return mListDatas;
    }
}
