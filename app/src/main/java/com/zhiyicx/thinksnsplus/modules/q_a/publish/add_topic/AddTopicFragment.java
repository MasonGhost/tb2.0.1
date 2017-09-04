package com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewEditorActionEvent;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.PublishContentActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.reward.QARewardActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment
        .BUNDLE_PUBLISHQA_BEAN;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

public class AddTopicFragment extends TSListFragment<AddTopicContract.Presenter, QATopicBean>
        implements AddTopicContract.View, MultiItemTypeAdapter.OnItemClickListener, TagFlowLayout
        .OnTagClickListener {

    @BindView(R.id.et_qustion)
    EditText mEtQustion;

    @BindView(R.id.line)
    View mLine;

    @BindView(R.id.fl_topics)
    TagFlowLayout mFLTopics;

    private String mQuestionStr = "";
    private List<QATopicBean> mQATopicBeanList = new ArrayList<>();
    private TopicsAdapter mTopicsAdapter;
    private int mMaxTagNums;

    private QAPublishBean mQAPublishBean;

    public static AddTopicFragment newInstance(Bundle args) {
        AddTopicFragment fragment = new AddTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_publish_qustion_add_topic;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_next);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_publish_select_topic_hint);
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        if (mQATopicBeanList.isEmpty()) {
            showSnackErrorMessage(getString(R.string.qa_publish_select_topic_hint));
            return;
        }
        saveQustion();
        Intent intent = new Intent(getActivity(), QARewardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_PUBLISHQA_BEAN, mQAPublishBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void saveQustion() {
        List<QAPublishBean.Topic> typeIdsList = new ArrayList<>();
        for (QATopicBean qaTopicBean : mQATopicBeanList) {
            QAPublishBean.Topic typeIds = new QAPublishBean.Topic();
            typeIds.setId(qaTopicBean.getId().intValue());
            typeIds.setName(qaTopicBean.getName());
            typeIdsList.add(typeIds);
        }
        mQAPublishBean.setTopics(typeIdsList);
//        mQAPublishBean.setSubject(mEtQustion.getText().toString());
        mPresenter.saveQuestion(mQAPublishBean);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        requestNetData(null, maxId, null, isLoadMore);
    }

    private void requestNetData(String name, Long maxId, Long follow, boolean isLoadMore) {
        mPresenter.requestNetData(name, maxId, follow, isLoadMore);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initTopicsView();

        RxTextView.editorActionEvents(mEtQustion).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                requestNetData(mEtQustion.getText().toString(), 0L, null, false);
            }
        });

    }

    private void initTopicsView() {
        mTopicsAdapter = new TopicsAdapter(mQATopicBeanList, getContext());
        mFLTopics.setAdapter(mTopicsAdapter);
        mFLTopics.setOnTagClickListener(this);
    }

    @Override
    protected void setLeftClick() {
        saveQustion();
        super.setLeftClick();
    }

    @Override
    public void onBackPressed() {
        setLeftClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        mQAPublishBean = mPresenter.getDraftQuestion(mQAPublishBean.getMark());
    }

    @Override
    protected void initData() {
        super.initData();
        mQAPublishBean = getArguments().getParcelable(BUNDLE_PUBLISHQA_BEAN);
//        mEtQustion.setText(RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, mQAPublishBean
//                .getSubject()));
        mMaxTagNums = getResources().getInteger(R.integer.tag_max_nums);

        QAPublishBean draft = mPresenter.getDraftQuestion(mQAPublishBean.getMark());
        if (draft != null) {
            List<QAPublishBean.Topic> topics = draft.getTopics();
            if (topics != null && !topics.isEmpty()) {
                mQATopicBeanList.clear();
                for (QAPublishBean.Topic topic : topics) {
                    QATopicBean qaTopicBean = new QATopicBean((long) topic.getId(), topic.getName
                            ());
                    mQATopicBeanList.add(qaTopicBean);
                }
                mTopicsAdapter.notifyDataChanged();
            }
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        AddTopicAdapter adapter = new AddTopicAdapter(getContext(), R.layout
                .item_publish_question_add_topic, mListDatas);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mQATopicBeanList.size() < mMaxTagNums) {
            if (mQATopicBeanList.contains(mListDatas.get(position))) {
                showSnackErrorMessage(getString(R.string.qa_publish_select_topic_repeat));
                return;
            }
            mQATopicBeanList.add(mListDatas.get(position));
            mTopicsAdapter.notifyDataChanged();
        } else {
            showSnackErrorMessage(getString(R.string.qa_publish_select_topic_count_hint,
                    mMaxTagNums));
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        mQATopicBeanList.remove(position);
        mTopicsAdapter.notifyDataChanged();
        return true;
    }

}
