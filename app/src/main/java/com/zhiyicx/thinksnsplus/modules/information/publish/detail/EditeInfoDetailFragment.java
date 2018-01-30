package com.zhiyicx.thinksnsplus.modules.information.publish.detail;

import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoContract;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoActivity;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownContract;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownFragment;

import java.util.Locale;

/**
 * @Author Jliuer
 * @Date 2018/01/18/9:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeInfoDetailFragment extends MarkdownFragment<PostDraftBean,EditeInfoDetailContract.Presenter>
        implements EditeInfoDetailContract.View{

    public static final String INFO_REFUSE = "info_refuse";
    public static InfoPublishBean mInfoPublishBean;

    /**
     * 提示信息弹窗
     */
    private ActionPopupWindow mInstructionsPopupWindow;

    /**
     * 取消提示选择弹框
     */
    private ActionPopupWindow mCanclePopupWindow;

    private boolean isRefuse;

    public static EditeInfoDetailFragment getInstance(Bundle bundle) {
        EditeInfoDetailFragment editeInfoDetailFragment = new EditeInfoDetailFragment();
        editeInfoDetailFragment.setArguments(bundle);
        return editeInfoDetailFragment;
    }

    @Override
    public boolean needSetting() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.edit_info);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.next);
    }

    @Override
    protected void initBundleDataWhenOnCreate() {
        super.initBundleDataWhenOnCreate();
        if (getArguments() != null) {
            mInfoPublishBean = getArguments().getParcelable(INFO_REFUSE);
        }
    }

    @Override
    protected boolean preHandlePublish() {
        if (mInfoPublishBean == null) {
            mInfoPublishBean = new InfoPublishBean();
        }
        return true;
    }

    @Override
    protected void handlePublish(String title, String markdwon, String noMarkdown) {
        super.handlePublish(title, markdwon, noMarkdown);
        mInfoPublishBean.setContent(markdwon);
        if(mPresenter==null){
            showSnackErrorMessage(getString(R.string.handle_fail));
            return;
        }
        mInfoPublishBean.setAmout(mPresenter.getSystemConfigBean().getNewsPayContribute());

        // 封面
        int cover;
        if (isRefuse) {
            cover = mInfoPublishBean.getCover();
        } else {
            cover = RegexUtils.getImageId(markdwon);
        }
        mInfoPublishBean.setCover(cover);
        mInfoPublishBean.setImage(cover < 0 ? null : (long) cover);

        mInfoPublishBean.setTitle(title);
        Intent intent = new Intent(getActivity(), AddInfoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void initEditWarningPop(String title, String html, String noMarkdown) {
        if (mCanclePopupWindow != null) {
            mCanclePopupWindow.show();
            return;
        }
        mCanclePopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_send_cancel_hint))
                .item2Str(getString(R.string.determine))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(() -> {
                    mInfoPublishBean = null;
                    mCanclePopupWindow.hide();
                    mActivity.finish();
                })
                .bottomClickListener(() -> mCanclePopupWindow.hide()).build();
        mCanclePopupWindow.show();
    }

    @Override
    protected void initPhotoPopupWindow() {
        if (mInsertedImages.size() + mFailedImages.size() >= 9) {
            initInstructionsPop(getString(R.string.instructions), String.format(Locale.getDefault
                    (), getString(R.string.choose_max_photos), 9));
            return;
        }
        super.initPhotoPopupWindow();
    }

    protected void initInstructionsPop(String title, String des) {
        if (mInstructionsPopupWindow != null) {
            mInstructionsPopupWindow.newBuilder().item1Str(title).desStr(des);
            mInstructionsPopupWindow.show();
            return;
        }
        mInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(title)
                .desStr(des)
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> {
                    mInstructionsPopupWindow.hide();
                })
                .build();
        mInstructionsPopupWindow.show();
    }

    @Override
    protected PostDraftBean getDraftData() {
        mDraftBean = new PostDraftBean();
        if (mInfoPublishBean != null) {
            mDraftBean.setTitle(mInfoPublishBean.getTitle());
            mDraftBean.setHtml(getHtml(mDraftBean.getTitle(), pareseBody(mInfoPublishBean.getContent())));
        }
        return mInfoPublishBean == null ? null : mDraftBean;
    }

    @Override
    protected void loadDraft(PostDraftBean postDraftBean) {
        super.loadDraft(postDraftBean);
        mRichTextView.loadDraft("", mDraftBean.getHtml());
    }

    @Override
    protected void pareseBodyResult() {
        mDraftBean.setFailedImages(mFailedImages);
        mDraftBean.setInsertedImages(mInsertedImages);
        mDraftBean.setImages(mImages);
    }
}
