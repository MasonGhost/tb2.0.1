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
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.reward.QARewardActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;
import static com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment.BUNDLE_PUBLISHQA_BEAN;


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

    private QAListInfoBean mQAListInfoBean;

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
        saveQuestion();
        if (PublishQuestionFragment.mDraftQuestion.isHasAgainEdite() && (PublishQuestionFragment.mDraftQuestion.getAmount() > 0 || PublishQuestionFragment.mDraftQuestion.isHasAdoption())) {
            PublishQuestionFragment.mDraftQuestion.setAmount(0);
            mPresenter.updateQuestion(PublishQuestionFragment.mDraftQuestion);
            return;
        }
        Intent intent = new Intent(getActivity(), QARewardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_PUBLISHQA_BEAN, PublishQuestionFragment.mDraftQuestion);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.DONE) {
            if (mQAListInfoBean != null) {
                goToQuestionDetail();
                getActivity().finish();
            }
        }
    }

    @Override
    public void updateSuccess(QAListInfoBean listInfoBean) {
        mQAListInfoBean = listInfoBean;
        mQAListInfoBean.setId(PublishQuestionFragment.mDraftQuestion.getId());
    }

    private void saveQuestion() {
        List<QAPublishBean.Topic> typeIdsList = new ArrayList<>();
        for (QATopicBean qaTopicBean : mQATopicBeanList) {
            QAPublishBean.Topic typeIds = new QAPublishBean.Topic();
            typeIds.setId(qaTopicBean.getId().intValue());
            typeIds.setName(qaTopicBean.getName());
            typeIdsList.add(typeIds);
        }
        PublishQuestionFragment.mDraftQuestion.setTopics(typeIdsList);
//        PublishQuestionFragment.mDraftQuestion.setSubject(mEtQustion.getText().toString());
        mPresenter.saveQuestion(PublishQuestionFragment.mDraftQuestion);
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
        mToolbarRight.setEnabled(false);
        mRvList.setOnTouchListener((v, event) -> {
            DeviceUtils.hideSoftKeyboard(AddTopicFragment.this.getActivity(), mEtQustion);
            return false;
        });
        RxTextView.editorActionEvents(mEtQustion).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                requestNetData(mEtQustion.getText().toString(), 0L, null, false);
            }
        });

        RxTextView.textChanges(mEtQustion)
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        // 清空输入框之后，加载全部数据
                        requestNetData(0L, false);
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
        saveQuestion();
        super.setLeftClick();
    }

    @Override
    public void onBackPressed() {
        setLeftClick();
    }

    @Override
    protected void initData() {
        super.initData();

        if (PublishQuestionFragment.mDraftQuestion.isHasAgainEdite() && PublishQuestionFragment.mDraftQuestion.getAmount() > 0) {
            mToolbarRight.setText(getString(R.string.publish));
        }

        mMaxTagNums = getResources().getInteger(R.integer.tag_max_nums);

        QAPublishBean draft = mPresenter.getDraftQuestion(PublishQuestionFragment.mDraftQuestion.getMark());
        if (draft != null) {
            List<QAPublishBean.Topic> topics = draft.getTopics();
            if (topics != null && !topics.isEmpty()) {
                mQATopicBeanList.clear();
                for (QAPublishBean.Topic topic : topics) {
                    QATopicBean qaTopicBean = new QATopicBean((long) topic.getId(), topic.getName
                            ());
                    mQATopicBeanList.add(qaTopicBean);
                }
                mToolbarRight.setEnabled(true);
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
        DeviceUtils.hideSoftKeyboard(AddTopicFragment.this.getActivity(), mEtQustion);
        if (mQATopicBeanList.size() < mMaxTagNums) {
            mToolbarRight.setEnabled(true);
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

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_PUBLISH_QUESTION)
    public void onPublishQuestionSuccess(Bundle bundle) {
        // 发布成功后关闭这个页面，暂时不传递数据
        getActivity().finish();
    }

    private void goToQuestionDetail() {
        if (mQAListInfoBean != null) {
            EventBus.getDefault().post(new Bundle(), EventBusTagConfig.EVENT_PUBLISH_QUESTION);
            PublishQuestionFragment.mDraftQuestion = null;
            Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(BUNDLE_QUESTION_BEAN, mQAListInfoBean);
            intent.putExtra(BUNDLE_QUESTION_BEAN, bundle);
            startActivity(intent);
        }
    }
}
