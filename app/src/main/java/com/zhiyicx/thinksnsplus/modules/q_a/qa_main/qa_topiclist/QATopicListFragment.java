package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        CommonAdapter adapter = new CommonAdapter<QATopicBean>(getContext(), R.layout.item_qatopic_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, QATopicBean o, int position) {
                holder.setText(R.id.tv_topic_feed_count,
                        String.format(Locale.getDefault(), getString(R.string.qa_show_topic_followed), 200, 41));
                ConvertUtils.stringLinkConvert(holder.getTextView(R.id.tv_topic_feed_count), setLinks(null));
                holder.setText(R.id.tv_topic_name, "大大大悟空");
                holder.setText(R.id.tv_topic_subscrib, getString(R.string.qa_topic_follow));
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
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

    private List<Link> setLinks(QATopicBean listInfoBean) {
        List<Link> links = new ArrayList<>();
        Link followCountLink = new Link("200").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(followCountLink);

        Link answerCountLink = new Link("41").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(answerCountLink);
        return links;
    }
}
