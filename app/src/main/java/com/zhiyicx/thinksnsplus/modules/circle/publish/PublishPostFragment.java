package com.zhiyicx.thinksnsplus.modules.circle.publish;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownFragment;

import java.util.ArrayList;

/**
 * @Author Jliuer
 * @Date 2017/12/19/14:40
 * @Email Jliuer@aliyun.com
 * @Description 发布帖子
 */
public class PublishPostFragment extends MarkdownFragment {

    public static final String BUNDLE_ISOUT_BOOLEAN = "isout";

    protected CircleInfo mCircleInfo;
    protected boolean isOutCirclePublish;
    protected PostPublishBean mPostPublishBean;

    public static PublishPostFragment newInstance(Bundle bundle) {
        PublishPostFragment markdownFragment = new PublishPostFragment();
        markdownFragment.setArguments(bundle);
        return markdownFragment;
    }

    @Override
    protected void initBundleDataWhenOnCreate() {
        super.initBundleDataWhenOnCreate();
        mCircleInfo = (CircleInfo) getArguments().getSerializable(BUNDLE_SOURCE_DATA);
        isOutCirclePublish = getArguments().getBoolean(BUNDLE_ISOUT_BOOLEAN);
    }

    @Override
    protected void initData() {
        super.initData();
        if (isOutCirclePublish) {
            mLlCircleContainer.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBottomMenu.setBottomMenuVisibleChangeListener(visible -> mCbSynToDynamic.setVisibility(visible ? View.VISIBLE : View.GONE));
    }

    @Override
    protected boolean preHandlePublish() {
        mPostPublishBean = new PostPublishBean();
        mImages = new ArrayList<>();
        return mCircleInfo != null;
    }

    @Override
    protected void handlePublish(String title, String markdwon, String noMarkdown) {
        super.handlePublish(title, markdwon, noMarkdown);
        mPostPublishBean.setTitle(title);
        mPostPublishBean.setBody(markdwon);
        mPostPublishBean.setSummary(noMarkdown);
        mPostPublishBean.setCircle_id(mCircleInfo.getId());
        mPostPublishBean.setSync_feed(mCbSynToDynamic.isChecked() ? 1 : 0);
        mPostPublishBean.setImages(mImages.toArray(new Integer[mImages.size()]));
        mPresenter.publishPost(mPostPublishBean);
    }

    @Override
    protected void onActivityResultForChooseCircle(CircleInfo circleInfo) {
        super.onActivityResultForChooseCircle(circleInfo);
        mCircleInfo = circleInfo;
        mCircleName.setText(mCircleInfo.getName());
    }

    @Override
    protected void saveDraft(String html) {
        super.saveDraft(html);
    }

    @Override
    protected void cancleEdit() {
        super.cancleEdit();
    }

    @Override
    protected void setRightClick() {
        mPostPublishBean = new PostPublishBean();
        mRichTextView.getResultWords(true);
    }

    @Override
    protected void setSynToDynamicCbVisiable(boolean isVisiable) {
        super.setSynToDynamicCbVisiable(isVisiable);
        if (mCircleInfo == null) {
            return;
        }
        mCbSynToDynamic.setVisibility(isVisiable && mCircleInfo.getAllow_feed() == 1 ? View.VISIBLE : View.GONE);
    }
}
