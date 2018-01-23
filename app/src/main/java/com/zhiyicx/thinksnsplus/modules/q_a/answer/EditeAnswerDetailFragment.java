package com.zhiyicx.thinksnsplus.modules.q_a.answer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.EditeQuestionDetailFragment;

/**
 * @Author Jliuer
 * @Date 2018/01/22/11:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeAnswerDetailFragment extends EditeQuestionDetailFragment {

    public static final String BUNDLE_SOURCE_ID = "source_id";
    public static final String BUNDLE_SOURCE_BODY = "source_body";
    public static final String BUNDLE_SOURCE_TYPE = "source_type";
    public static final String BUNDLE_SOURCE_ANONYMITY = "source_anonymity";
    public static final String BUNDLE_SOURCE_MARK = "source_mark";

    /**
     * 发布回答的提示语是问题的标题
     */
    public static final String BUNDLE_SOURCE_TITLE = "source_title";

    private PublishType mType;
    private String mBody;
    private String mTitle;

    public static EditeAnswerDetailFragment newInstance(Bundle bundle) {
        if (bundle == null || bundle.getLong(BUNDLE_SOURCE_ID) <= 0) {
            throw new IllegalArgumentException("question_id can not be null");
        }
        EditeAnswerDetailFragment publishContentFragment = new EditeAnswerDetailFragment();
        publishContentFragment.setArguments(bundle);
        return publishContentFragment;
    }

    @Override
    protected boolean leftClickNeedMarkdown() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_publish_answer);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_btn);
    }

    @Override
    protected String setInputInitText() {
        return getString(R.string.qa_answer_content_hint);
    }

    @Override
    protected boolean openDraft() {
        return mType != PublishType.UPDATE_ANSWER;
    }

    @Override
    protected void handlePublish(String title, String markdwon, String noMarkdown) {
        if (mType == PublishType.PUBLISH_ANSWER) {
            mPresenter.publishAnswer(getArguments().getLong(BUNDLE_SOURCE_ID), markdwon
                    , mAnonymity);
        } else if (mType == PublishType.UPDATE_ANSWER) {
            mPresenter.updateAnswer(getArguments().getLong(BUNDLE_SOURCE_ID), markdwon,
                    mAnonymity);
        } else if (mType == PublishType.UPDATE_QUESTION) {
            mPresenter.updateQuestion(getArguments().getLong(BUNDLE_SOURCE_ID), markdwon,
                    mAnonymity);
        }
    }

    @Override
    protected void initData() {
        mBody = getArguments().getString(BUNDLE_SOURCE_BODY, "");
        mType = (PublishType) getArguments().getSerializable(BUNDLE_SOURCE_TYPE);
        mTitle = getArguments().getString(BUNDLE_SOURCE_TITLE, "");
        mAnonymity = getArguments().getInt(BUNDLE_SOURCE_ANONYMITY, 0);
        if (mType == PublishType.PUBLISH_ANSWER) {
            mToolbarCenter.setText(getString(R.string.qa_publish_answer));
        } else if (mType == PublishType.UPDATE_ANSWER) {
            mToolbarCenter.setText(getString(R.string.qa_update_answer));
        } else if (mType == PublishType.UPDATE_QUESTION) {
            mToolbarCenter.setText(getString(R.string.qa_update_publish));
        }
        super.initData();
    }

    @Override
    protected PostDraftBean getDraftData() {
        if (!TextUtils.isEmpty(mBody)) {
            mDraftBean = new PostDraftBean();
            mDraftBean.setHtml(getHtml("", pareseBody(mBody)));
        }
        return mDraftBean;
    }

    @Override
    protected void saveDraft(String title, String html, String noMarkdown) {
        super.saveDraft(title, html, noMarkdown);
        long draftMark = getArguments().getLong(BUNDLE_SOURCE_MARK);
        AnswerDraftBean answerDraftBean = new AnswerDraftBean();
        long mark = Long.parseLong(AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System
                .currentTimeMillis());
        // 覆盖草稿
        if (draftMark != 0) {
            mark = draftMark;
        }
        answerDraftBean.setMark(mark);
        answerDraftBean.setId(getArguments().getLong(BUNDLE_SOURCE_ID));
        answerDraftBean.setBody(html);
        answerDraftBean.setAnonymity(mAnonymity);
        answerDraftBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        mPresenter.saveAnswer(answerDraftBean);
        mEditWarningPopupWindow.hide();
        getActivity().finish();
    }

    @Override
    public void publishSuccess(AnswerInfoBean answerBean) {
        super.publishSuccess(answerBean);
        AnswerDraftBean draftBean = new AnswerDraftBean();
        draftBean.setMark(getArguments().getLong(BUNDLE_SOURCE_MARK));
        mPresenter.deleteAnswer(draftBean);
    }

    @Override
    public void updateSuccess() {
        super.updateSuccess();
        AnswerDraftBean draftBean = new AnswerDraftBean();
        draftBean.setMark(getArguments().getLong(BUNDLE_SOURCE_MARK));
        mPresenter.deleteAnswer(draftBean);
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.DONE) {
            getActivity().finish();
        }
    }

    @Override
    protected boolean showAnonymityAlertPopWindow() {
        return mAnonymity != 1;
    }

    /**
     * @param context
     * @param type
     * @param sourceId
     * @param body
     */
    public static void startQActivity(Context context, PublishType type, long sourceId,
                                      String body, String title, int anonymity) {

        Intent intent = new Intent(context, EditeAnswerDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_SOURCE_TYPE, type);
        bundle.putLong(BUNDLE_SOURCE_ID, sourceId);
        bundle.putInt(BUNDLE_SOURCE_ANONYMITY, anonymity);
        bundle.putString(BUNDLE_SOURCE_BODY, body);
        bundle.putString(BUNDLE_SOURCE_TITLE, title);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startQActivity(Context context, PublishType type, AnswerDraftBean realData) {

        Intent intent = new Intent(context, EditeAnswerDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_SOURCE_TYPE, type);
        bundle.putLong(BUNDLE_SOURCE_ID, realData.getId());
        bundle.putLong(BUNDLE_SOURCE_MARK, realData.getMark());
        bundle.putString(BUNDLE_SOURCE_BODY, realData.getBody());
        bundle.putString(BUNDLE_SOURCE_TITLE, "");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
