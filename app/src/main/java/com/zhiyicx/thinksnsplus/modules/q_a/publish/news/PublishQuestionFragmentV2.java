package com.zhiyicx.thinksnsplus.modules.q_a.publish.news;

import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic.AddTopicActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment;

/**
 * @Author Jliuer
 * @Date 2018/01/19/11:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishQuestionFragmentV2 extends MarkdownFragment<PostDraftBean> {

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
    protected void initData() {
        super.initData();
        mRichTextView.hideTitle();
    }

    @Override
    protected boolean preHandlePublish() {
        return true;
    }

    @Override
    protected void handlePublish(String title, String markdwon, String noMarkdown) {
        super.handlePublish(title, markdwon, noMarkdown);
        PublishQuestionFragment.mDraftQuestion.setBody(markdwon);
        Intent intent = new Intent(getActivity(), AddTopicActivity.class);
        startActivity(intent);
    }

    @Override
    public void onInputListener(int titleLength, int contentLength) {
        super.onInputListener(titleLength, contentLength);
        setRightClickable(contentLength > 0);
    }
}
