package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
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
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container.QATopicFragmentContainerFragment.TOPIC_TYPE_FOLLOW;

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
    public String mTopictype = "";

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
    protected boolean setUseStatusView() {
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
            protected void convert(ViewHolder holder, QATopicBean topicBean, int position) {
                ImageView headImage = holder.getImageViwe(R.id.iv_topic_cover);
                CheckBox subscrib = holder.getView(R.id.tv_topic_subscrib);
                holder.setText(R.id.tv_topic_feed_count,
                        String.format(Locale.getDefault(), getString(R.string.qa_show_topic_followed),
                                topicBean.getFollows_count(), topicBean.getQuestions_count()));
                ConvertUtils.stringLinkConvert(holder.getTextView(R.id.tv_topic_feed_count), setLinks(topicBean));
                holder.setText(R.id.tv_topic_name, topicBean.getName());
                holder.setText(R.id.tv_topic_subscrib, getString(R.string.qa_topic_follow));
                Glide.with(getContext())
                        .load(topicBean.getAvatar())
                        .placeholder(R.drawable.shape_default_image)
                        .error(R.drawable.shape_default_image)
                        .into(headImage);

                // 设置订阅状态
                boolean isJoined = topicBean.getHas_follow();
                subscrib.setChecked(isJoined);
                subscrib.setText(isJoined ? getContext().getString(R.string.qa_topic_followed) : getContext().getString(R.string.qa_topic_follow));
                subscrib.setPadding(isJoined ? getContext().getResources().getDimensionPixelSize(R.dimen.spacing_small) : getContext().getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, 0, 0);
                RxView.clicks(subscrib)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> {
                            if (TouristConfig.CHEENAL_CAN_SUBSCRIB || !mPresenter.handleTouristControl()) {
                                mPresenter.handleTopicFollowState(position,topicBean.getId()+"", isJoined);
                            } else {
                                subscrib.setChecked(false);
                            }
                        });
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
        mTopictype = getArguments().getString(BUNDLE_TOPIC_TYPE);
        super.initData();

    }

    @Override
    public String getType() {
        return mTopictype;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        if (mTopictype.equals(TOPIC_TYPE_FOLLOW)) {
            requestNetData(mTopictype, maxId, isLoadMore);
        } else {
            requestNetData(null, maxId, 1L, isLoadMore);
        }

    }

    private void requestNetData(String type, Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(type, maxId, isLoadMore);
    }

    private void requestNetData(String name, Long maxId, Long follow, boolean isLoadMore) {
        mPresenter.requestNetData(name, maxId, follow, isLoadMore);
    }

    @Override
    protected List<QATopicBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return mListDatas;
    }

    private List<Link> setLinks(QATopicBean topicBean) {
        List<Link> links = new ArrayList<>();
        Link followCountLink = new Link(topicBean.getFollows_count() + "").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(followCountLink);

        Link answerCountLink = new Link(topicBean.getQuestions_count() + "").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(answerCountLink);
        return links;
    }
}
