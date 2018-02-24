package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.zhiyicx.baseproject.widget.popwindow.CenterAlertPopWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic.AddTopicActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2018/01/19/11:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeQuestionDetailFragment extends MarkdownFragment<PostDraftBean, EditeQuestionDetailContract.Presenter>
        implements EditeQuestionDetailContract.View {

    /**
     * 匿名提示
     */
    @BindView(R.id.ll_anony)
    LinearLayout mLLAnony;
    @BindView(R.id.switch_anony)
    SwitchCompat mSwitchAnony;

    protected boolean isBack;

    protected CenterAlertPopWindow mAnonymityAlertPopWindow;
    protected int mAnonymity;

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_next);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_detail);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    public static EditeQuestionDetailFragment newInstance(Bundle bundle) {
        EditeQuestionDetailFragment markdownFragment = new EditeQuestionDetailFragment();
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
        super.onAfterInitialLoad(ready);
        if (ready) {
            mRichTextView.hideTitle();
        }
    }

    @Override
    public void onSettingImageButtionClick(boolean isSelected) {
        super.onSettingImageButtionClick(isSelected);
        setAnnoyVisible(isSelected);
    }

    @Override
    protected boolean leftClickNeedMarkdown() {
        return true;
    }

    @Override
    protected boolean preHandlePublish() {
        return true;
    }

    @Override
    protected void handlePublish(String title, String markdwon, String noMarkdown, String html) {
        super.handlePublish(title, markdwon, noMarkdown, html);
        PublishQuestionFragment.mDraftQuestion.setBody(markdwon);
        if (!isBack) {
            Intent intent = new Intent(getActivity(), AddTopicActivity.class);
            startActivity(intent);
        } else {
            mActivity.finish();
        }

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

    @Override
    public boolean needSetting() {
        return true;
    }

    @Override
    public void publishSuccess(AnswerInfoBean answerBean) {

    }

    @Override
    public void updateSuccess() {

    }

    @Override
    protected void initListener() {
        super.initListener();
        mSwitchAnony.setOnCheckedChangeListener((view, isCheck) -> initAnonymityAlertPopWindow(isCheck));
    }

    protected void setAnnoyVisible(boolean visible) {
        boolean isAnnoy = mAnonymity == 1 || PublishQuestionFragment.mDraftQuestion != null
                && PublishQuestionFragment.mDraftQuestion.getAnonymity() == 1;
        mSwitchAnony.setChecked(isAnnoy);
        mLLAnony.setVisibility(visible ? View.VISIBLE : View.GONE);

    }

    protected void initAnonymityAlertPopWindow(boolean isChecked) {
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        if (mAnonymityAlertPopWindow == null) {
            mAnonymityAlertPopWindow = CenterAlertPopWindow.builder()
                    .with(getActivity())
                    .parentView(getView())
                    .isFocus(false)
                    .animationStyle(R.style.style_actionPopupAnimation)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .titleStr(getString(R.string.qa_publish_enable_anonymous))
                    .desStr(TextUtils.isEmpty(mSystemConfigBean.getAnonymityRule()) ? "" : mSystemConfigBean.getAnonymityRule())
                    .buildCenterPopWindowItem1ClickListener(new CenterAlertPopWindow.CenterPopWindowItemClickListener() {
                        @Override
                        public void onRightClicked() {
                            View view = getActivity().getWindow().peekDecorView();
                            if (view != null) {
                                InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            mAnonymityAlertPopWindow.dismiss();
                            mAnonymity = 1;
                            if (PublishQuestionFragment.mDraftQuestion != null) {
                                PublishQuestionFragment.mDraftQuestion.setAnonymity(1);
                            }
                        }

                        @Override
                        public void onLeftClicked() {
                            mAnonymityAlertPopWindow.dismiss();
                            if (PublishQuestionFragment.mDraftQuestion != null) {
                                PublishQuestionFragment.mDraftQuestion.setAnonymity(0);
                            }
                            mCbSynToDynamic.setChecked(false);
                            mAnonymity = 0;
                            setAnnoyVisible(true);
                        }
                    })
                    .build();
        }
        if (isChecked && showAnonymityAlertPopWindow()) {
            mAnonymityAlertPopWindow.show();
        } else {
            mAnonymityAlertPopWindow.dismiss();
        }
        mAnonymity = isChecked ? 1 : 0;
        if (PublishQuestionFragment.mDraftQuestion != null) {
            PublishQuestionFragment.mDraftQuestion.setAnonymity(mAnonymity);
        }
    }

    protected boolean showAnonymityAlertPopWindow() {
        return true;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_PUBLISH_QUESTION)
    public void onPublishQuestionSuccess(Bundle bundle) {
        // 发布成功后关闭这个页面
        getActivity().finish();
    }

}
