package com.zhiyicx.thinksnsplus.modules.q_a.publish.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic.AddTopicActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;

/**
 * @Author Jliuer
 * @Date 2018/01/19/11:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishQuestionFragmentV2 extends MarkdownFragment<PostDraftBean> {

    private boolean isBack;

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_next);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_detail);
    }

    public static PublishQuestionFragmentV2 newInstance(Bundle bundle) {
        PublishQuestionFragmentV2 markdownFragment = new PublishQuestionFragmentV2();
        markdownFragment.setArguments(bundle);
        return markdownFragment;
    }

    @Override
    protected PostDraftBean getDraftData() {
        if (PublishQuestionFragment.mDraftQuestion != null && !TextUtils.isEmpty(PublishQuestionFragment.mDraftQuestion.getBody())) {
            mDraftBean = new PostDraftBean();
            mDraftBean.setHtml(getHtml("", pareseBody(PublishQuestionFragment.mDraftQuestion.getBody())));
        }
        return mDraftBean;
    }

    @Override
    protected boolean openDraft() {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBack = true;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        isBack = false;
    }

    @Override
    protected void loadDraft(PostDraftBean postDraftBean) {
        super.loadDraft(postDraftBean);
        mRichTextView.loadDraft("", mDraftBean.getHtml());
    }

    @Override
    public void onAfterInitialLoad(boolean ready) {
        if (ready) {
            mRichTextView.hideTitle();
        }
    }

    @Override
    protected boolean preHandlePublish() {
        return true;
    }

    @Override
    protected void handlePublish(String title, String markdwon, String noMarkdown) {
        super.handlePublish(title, markdwon, noMarkdown);
        if (!isBack) {
            PublishQuestionFragment.mDraftQuestion.setBody(markdwon);
            Intent intent = new Intent(getActivity(), AddTopicActivity.class);
            startActivity(intent);
        } else {
            onBackPressed();
        }

    }

    @Override
    protected boolean contentIsNull(String title, String markdwon, String noMarkdown) {
        PublishQuestionFragment.mDraftQuestion.setBody(markdwon);
        return super.contentIsNull(title, markdwon, noMarkdown);
    }

    @Override
    public void onInputListener(int titleLength, int contentLength) {
        super.onInputListener(titleLength, contentLength);
        setRightClickable(contentLength > 0);
    }

    @Override
    protected void pareseBodyResult() {
        mDraftBean.setFailedImages(mFailedImages);
        mDraftBean.setInsertedImages(mInsertedImages);
        mDraftBean.setImages(mImages);
    }

}
