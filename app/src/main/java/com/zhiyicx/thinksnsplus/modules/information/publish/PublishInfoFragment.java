package com.zhiyicx.thinksnsplus.modules.information.publish;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.richtext.DataImageView;
import com.zhiyicx.thinksnsplus.modules.q_a.richtext.RichTextEditor;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoFragment.BUNDLE_PUBLISH_BEAN;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:43
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishInfoFragment extends TSFragment<PublishInfoContract.Presenter>
        implements PublishInfoContract.View, PhotoSelectorImpl.IPhotoBackListener,
        RichTextEditor.OnContentChangeListener {

    public static final String INFO_REFUSE = "info_refuse";
    public static InfoPublishBean sInfoPublishBean;

    @BindView(R.id.et_info_title)
    UserInfoInroduceInputView mEtInfoTitle;
    @BindView(R.id.riche_test)
    RichTextEditor mRicheTest;
    @BindView(R.id.im_arrowc)
    ImageView mImArrowc;
    @BindView(R.id.im_pic)
    ImageView mImPic;
    @BindView(R.id.im_setting)
    ImageView mImSetting;
    @BindView(R.id.rl_publish_tool)
    RelativeLayout mRlPublishTool;
    @BindView(R.id.pb_image_upload)
    LinearLayout mPbImageUpload;

    private PhotoSelectorImpl mPhotoSelector;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private ActionPopupWindow mCanclePopupWindow;// 取消提示选择弹框

    private DataImageView mSubsamplingScaleImageView;

    private int mPicTag;// 记录上传成功的张数

    private ActionPopupWindow mInstructionsPopupWindow;

    public static PublishInfoFragment getInstance(Bundle bundle) {
        PublishInfoFragment publishInfoFragment = new PublishInfoFragment();
        publishInfoFragment.setArguments(bundle);
        return publishInfoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sInfoPublishBean = getArguments().getParcelable(INFO_REFUSE);
        } else {
            sInfoPublishBean = new InfoPublishBean();
        }
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.edit_info);
    }

    @Override
    protected String setLeftTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected void setLeftClick() {
        handleBack();
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.next);
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();

        String content = getContentString();
        sInfoPublishBean.setContent(content);
        sInfoPublishBean.setAmout(mPresenter.getSystemConfigBean().getNewsPayContribute());
        long cover;
        if (sInfoPublishBean.isRefuse()) {
            cover = sInfoPublishBean.getCover();
        } else {
            cover = RegexUtils.getImageId(content);
        }
        sInfoPublishBean.setCover((int) cover);
        sInfoPublishBean.setImage(cover < 0 ? null : cover);
        sInfoPublishBean.setTitle(mEtInfoTitle.getInputContent());
        if (!TextUtils.isEmpty(sInfoPublishBean.getContent()) && !TextUtils.isEmpty(sInfoPublishBean.getTitle())) {
            Intent intent = new Intent(getActivity(), AddInfoActivity.class);
            startActivity(intent);
        } else {
            showSnackErrorMessage(getString(R.string.info_title_necessary));
        }

    }

    @NonNull
    private String getContentString() {
        StringBuilder builder = new StringBuilder();
        List<RichTextEditor.EditData> datas = mRicheTest.buildEditData();
        for (RichTextEditor.EditData editData : datas) {
            builder.append(editData.inputStr);
            if (!editData.imagePath.isEmpty()) {
                if (editData.imagePath.contains("![image]")) {
                    builder.append(editData.imagePath);
                } else {
                    builder.append(String.format(Locale.getDefault(),
                            MarkdownConfig.IMAGE_TAG, MarkdownConfig.IMAGE_TITLE,
                            editData.imageId));
                }
            }
        }
        return builder.toString();
    }

    @Override
    protected void initView(View rootView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            AndroidBug5497Workaround.assistActivity(getActivity());
        }
        checkNextBtEnabel(false);
        mToolbarLeft.setTextColor(SkinUtils.getColor(R.color.themeColor));
        initLisenter();
        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) mImPic.getLayoutParams();
        layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mImPic.setLayoutParams(layout);
        mImSetting.setVisibility(View.GONE);
    }

    private void checkNextBtEnabel(boolean enabled) {
        mToolbarRight.setEnabled(enabled && !TextUtils.isEmpty(mEtInfoTitle.getInputContent()));
    }

    @Override
    protected void initData() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
        if (sInfoPublishBean != null && !TextUtils.isEmpty(sInfoPublishBean.getContent())
                && !TextUtils.isEmpty(sInfoPublishBean.getContent()) && mRicheTest != null) {
            mRicheTest.getAllLayout().post(() -> {
                mRicheTest.clearAllLayout();
                mPresenter.pareseBody(sInfoPublishBean.getContent());
                mEtInfoTitle.setText(sInfoPublishBean.getTitle());
            });
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_publish_info;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (photoList.isEmpty()) {
            return;
        }
        mPbImageUpload.setVisibility(View.VISIBLE);
        String path = photoList.get(0).getImgUrl();
        if (mPresenter != null) {
            mPresenter.uploadPic(path, "", true, 0, 0);
        }
        mSubsamplingScaleImageView = mRicheTest.insertImage(path, mRicheTest.getWidth());
        mPicTag++;
        LogUtils.d("uploadPicSuccess::" + mPicTag);
    }

    @Override
    public void addImageViewAtIndex(String iamge, int iamge_id, String markdonw, boolean isLast) {
        mPicTag++;
        mRicheTest.updateImageViewAtIndex(mRicheTest.getLastIndex(), iamge_id, iamge, markdonw, isLast);
    }

    @Override
    public void addEditTextAtIndex(String text) {
        mRicheTest.updateEditTextAtIndex(mRicheTest.getLastIndex(), text);
    }

    @Override
    public void onPareseBodyEnd(boolean hasContent) {
        checkNextBtEnabel(hasContent);
        mRicheTest.setHasContent(hasContent);
    }

    @Override
    public void uploadPicSuccess(int id) {
        mPbImageUpload.setVisibility(View.GONE);
        mSubsamplingScaleImageView.setId(id);
    }

    @Override
    public void uploadPicFailed() {
        mPbImageUpload.setVisibility(View.GONE);
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        if (prompt == Prompt.ERROR) {
            mRicheTest.deleteImage();
        }
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void publishInfoFailed() {

    }

    @Override
    public void publishInfoSuccess() {

    }

    @Override
    public void onImageDelete() {
        if (mPicTag > 0) {
            mPicTag--;
        }
        LogUtils.d("onImageDelete::" + mPicTag);
    }

    @Override
    public void onContentChange(boolean hasContent) {
        checkNextBtEnabel(hasContent);
    }

    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {
        if (mPicTag == 9) {
            initInstructionsPop(getString(R.string.instructions), String.format(Locale.getDefault(), getString(R.string.choose_max_photos), 9));
            return;
        }
        if (mPhotoPopupWindow != null) {
            mPhotoPopupWindow.show();
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.choose_from_photo))
                .item2Str(getString(R.string.choose_from_camera))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 选择相册，单张
                    mPhotoSelector.getPhotoListFromSelector(1, null);
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(null);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
        mPhotoPopupWindow.show();
    }

    @OnClick({R.id.im_arrowc, R.id.im_pic, R.id.im_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_arrowc:
                mRicheTest.hideKeyBoard();
                break;
            case R.id.im_pic:
                mRicheTest.hideKeyBoard();
                initPhotoPopupWindow();
                break;
            case R.id.im_setting:
                break;

                default:
        }
    }

    @Override
    public boolean showUplaoding() {
        return false;
    }

    private void initLisenter() {
        RxView.globalLayouts(mRlPublishTool).subscribe(aVoid -> {
            if (mRicheTest == null) {
                return;
            }
            int[] viewLacotion = new int[2];
            mRlPublishTool.getLocationOnScreen(viewLacotion);
            if (viewLacotion[1] > mRlPublishTool.getHeight()) {
                View rootview = getActivity().getWindow().getDecorView();
                View currentEdit = rootview.findFocus();
                if (currentEdit != null) {
                    int[] currentEditLacotion = new int[2];
                    currentEdit.getLocationOnScreen(currentEditLacotion);
                    int dy = currentEditLacotion[1] - viewLacotion[1] + currentEdit.getHeight();
                    mRicheTest.smoothScrollBy(0, dy);
                }
            }
        });

        mPbImageUpload.setOnTouchListener((v, event) -> true);

        mEtInfoTitle.setContentChangedListener(s ->
                checkNextBtEnabel(s.length() > 0 && mRicheTest.isHasContent())
        );
        mRicheTest.setOnContentEmptyListener(this);
    }

    public void initInstructionsPop(String title, String des) {
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
                .bottomClickListener(() -> mInstructionsPopupWindow.hide())
                .build();
        mInstructionsPopupWindow.show();
    }

    /**
     * 初始化取消选择弹框
     */
    private void initCanclePopupWindow() {
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
                    mCanclePopupWindow.hide();
                    sInfoPublishBean = null;
                    getActivity().finish();
                })
                .bottomClickListener(() -> mCanclePopupWindow.hide()).build();
        mCanclePopupWindow.show();
    }

    private void handleBack() {
        if (!TextUtils.isEmpty(mEtInfoTitle.getEtContent().getText()) || mRicheTest.isHasContent()) {
            mRicheTest.hideKeyBoard();
            initCanclePopupWindow();
        } else {
            super.setLeftClick();
        }
    }

}
