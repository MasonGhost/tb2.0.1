package com.zhiyicx.thinksnsplus.modules.circle.publish;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownContract;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownFragment;

/**
 * @Author Jliuer
 * @Date 2017/12/19/14:40
 * @Email Jliuer@aliyun.com
 * @Description 发布帖子
 */
public class PublishPostFragment extends MarkdownFragment<PostDraftBean,PublishPostContract.Presenter> implements PublishPostContract.View{

    public static final String BUNDLE_ISOUT_BOOLEAN = "isout";
    public static final String BUNDLE_DRAFT_DATA = "draft";

    protected CircleInfo mCircleInfo;
    protected boolean isOutCirclePublish;
    protected PostPublishBean mPostPublishBean;
    protected boolean isBttomMenuVisible = true;

    public static PublishPostFragment newInstance(Bundle bundle) {
        PublishPostFragment markdownFragment = new PublishPostFragment();
        markdownFragment.setArguments(bundle);
        return markdownFragment;
    }

    @Override
    protected void initBundleDataWhenOnCreate() {
        super.initBundleDataWhenOnCreate();
        mCircleInfo = getArguments().getParcelable(BUNDLE_SOURCE_DATA);
        mDraftBean = getArguments().getParcelable(BUNDLE_DRAFT_DATA);
        isOutCirclePublish = getArguments().getBoolean(BUNDLE_ISOUT_BOOLEAN);
        if (mDraftBean != null && mDraftBean.getCircleInfo() != null) {
            // 草稿视为圈外发帖
            mCircleInfo = mDraftBean.getCircleInfo();
            isOutCirclePublish = true;
        }
    }

    @Override
    protected void restoreImageData() {
        if (mDraftBean != null) {
            if (mDraftBean.getInsertedImages() != null) {
                mInsertedImages = mDraftBean.getInsertedImages();
            }
            if (mDraftBean.getFailedImages() != null) {
                mFailedImages = mDraftBean.getFailedImages();
            }
            if (mDraftBean.getImages() != null) {
                mImages = mDraftBean.getImages();
            }
        }
        mRichTextView.addImageClickListener(getImageIds());
    }

    @Override
    protected void initData() {
        super.initData();
        if (isOutCirclePublish) {
            mLlCircleContainer.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
        }
        if (mDraftBean != null) {
            mContentLength = mDraftBean.getTitle().length() * mDraftBean.getHtml().length();
        }
        if (mCircleInfo != null) {
            mCircleName.setText(mCircleInfo.getName());
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        setSynToDynamicCbVisiable(true);
        mBottomMenu.setBottomMenuVisibleChangeListener(this::setSynToDynamicCbVisiable);
        RxTextView.textChanges(mCircleName).subscribe(charSequence -> setRightClickable(mContentLength > 0));
    }

    @Override
    protected void setRightClickable(boolean clickable) {
        super.setRightClickable(clickable);
        if (isOutCirclePublish) {
            mToolbarRight.setEnabled(clickable && mCircleName.getText().toString().length() > 0);
        }
    }

    @Override
    protected boolean canGotoCircle() {
        return isOutCirclePublish;
    }

    @Override
    protected boolean preHandlePublish() {
        mPostPublishBean = new PostPublishBean();
        if (mCircleInfo == null) {
            showSnackErrorMessage(getString(R.string.post_publish_select_circle));
        }
        return mCircleInfo != null;
    }

    @Override
    protected boolean openDraft() {
        return true;
    }


    @Override
    protected void loadDraft(PostDraftBean postDraftBean) {
        mRichTextView.loadDraft(mDraftBean.getTitle(), mDraftBean.getHtml());
        mCbSynToDynamic.setChecked(mDraftBean.getHasSynToDynamic());
    }

    @Override
    protected void handlePublish(String title, String markdwon, String noMarkdown,String html) {
        super.handlePublish(title, markdwon, noMarkdown,html);
        if (TextUtils.isEmpty(title)) {
            showSnackErrorMessage(getString(R.string.post_publish_select_title));
            return;
        }
        if (TextUtils.isEmpty(markdwon)) {
            showSnackErrorMessage(getString(R.string.post_publish_select_content));
            return;
        }
        mPostPublishBean.setTitle(title);
        mPostPublishBean.setBody(markdwon);
        mPostPublishBean.setSummary(noMarkdown);
        mPostPublishBean.setCircle_id(mCircleInfo.getId());
        mPostPublishBean.setSync_feed(mCbSynToDynamic.isChecked() ? 1 : 0);
        mPostPublishBean.setFeed_from(mCbSynToDynamic.isChecked() ? 4 : 0);
        mPostPublishBean.setImages(mImages);
        mPresenter.publishPost(mPostPublishBean);
    }

    @Override
    protected void onActivityResultForChooseCircle(CircleInfo circleInfo) {
        super.onActivityResultForChooseCircle(circleInfo);
        mCircleInfo = circleInfo;
        mCircleName.setText(mCircleInfo.getName());
    }

    @Override
    public void onBottomMenuVisibleChange(boolean visible) {
        super.onBottomMenuVisibleChange(visible);
        isBttomMenuVisible = visible;
        setSynToDynamicCbVisiable(visible);
    }

    @Override
    protected void saveDraft(String title, String html, String noMarkdown) {
        PostDraftBean postDraftBean = new PostDraftBean();
        long mark;
        if (mDraftBean != null) {
            mark = mDraftBean.getMark();
        } else {
            mark = Long.parseLong(AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System
                    .currentTimeMillis());
        }
        postDraftBean.setMark(mark);
        postDraftBean.setImages(mImages);
        postDraftBean.setInsertedImages(mInsertedImages);
        postDraftBean.setFailedImages(mFailedImages);
        postDraftBean.setHasSynToDynamic(mCbSynToDynamic.isChecked());
        postDraftBean.setTitle(title);
        postDraftBean.setCircleInfo(mCircleInfo);
        postDraftBean.setCreate_at(TimeUtils.getCurrenZeroTimeStr());
        postDraftBean.setContent(noMarkdown);
        postDraftBean.setHtml("<!DOCTYPE html>\n" + html);
        postDraftBean.setIsOutCircle(isOutCirclePublish);
        mPresenter.saveDraft(postDraftBean);
    }

    @Override
    public boolean needSetting() {
        return false;
    }

    @Override
    protected void cancleEdit() {
        super.cancleEdit();
    }

    @Override
    protected PostDraftBean getDraftData() {
        return mDraftBean;
    }

    @Override
    protected void setSynToDynamicCbVisiable(boolean isVisiable) {
        super.setSynToDynamicCbVisiable(isVisiable);
        if (mCircleInfo == null || mCbSynToDynamic == null) {
            return;
        }
        mCbSynToDynamic.setVisibility(isVisiable && mCircleInfo.getAllow_feed() == 1 ? View
                .VISIBLE : View.GONE);
    }

    @Override
    public void sendPostSuccess(CirclePostListBean data) {
        CirclePostDetailActivity.startActivity(getActivity(), data.getGroup_id(), data.getId(),
                false, canGotoCircle());
        getActivity().finish();
    }
}
