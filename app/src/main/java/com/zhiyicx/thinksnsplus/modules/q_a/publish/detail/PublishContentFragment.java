package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.zhiyicx.baseproject.widget.popwindow.AnonymityPopWindow;
import com.zhiyicx.baseproject.widget.popwindow.CenterAlertPopWindow;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic.AddTopicActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.richtext.DataImageView;
import com.zhiyicx.thinksnsplus.modules.q_a.richtext.RichTextEditor;
import com.zhiyicx.thinksnsplus.utils.DealPhotoUtils;

import org.simple.eventbus.Subscriber;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment.BUNDLE_PUBLISHQA_BEAN;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishContentFragment extends TSFragment<PublishContentConstact.Presenter> implements
        PublishContentConstact.View, PhotoSelectorImpl.IPhotoBackListener, RichTextEditor.OnContentChangeListener {


    @BindView(R.id.riche_test)
    protected RichTextEditor mRicheTest;
    @BindView(R.id.im_arrowc)
    ImageView mImArrowc;
    @BindView(R.id.im_pic)
    ImageView mImPic;
    @BindView(R.id.im_setting)
    ImageView mImSetting;
    @BindView(R.id.pb_image_upload)
    LinearLayout mPbImageUpload;
    @BindView(R.id.rl_publish_tool)
    RelativeLayout mRlPublishTool;

    protected PhotoSelectorImpl mPhotoSelector;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private ActionPopupWindow mInstructionsPopupWindow;
    private int mPicTag;// 已经添加的图片数量
    protected int mAnonymity;

    private AnonymityPopWindow mAnonymityPopWindow;
    private CenterAlertPopWindow mAnonymityAlertPopWindow;

    private QAPublishBean mQAPublishBean;
    private DataImageView test;

    public static PublishContentFragment newInstance(Bundle bundle) {
        PublishContentFragment publishContentFragment = new PublishContentFragment();
        publishContentFragment.setArguments(bundle);
        return publishContentFragment;
    }

    @Override
    public void publishSuccess(AnswerInfoBean answerBean) {

    }

    @Override
    public void updateSuccess() {

    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_next);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_detail);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            AndroidBug5497Workaround.assistActivity(mActivity);
        }
        mToolbarRight.setEnabled(false);
        initLisenter();
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        saveQuestion();
        Intent intent = new Intent(getActivity(), AddTopicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_PUBLISHQA_BEAN, mQAPublishBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void setLeftClick() {
        saveQuestion();
        super.setLeftClick();
    }

    @Override
    public void onBackPressed() {
        saveQuestion();
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mQAPublishBean != null) {
            mQAPublishBean = mPresenter.getDraftQuestion(mQAPublishBean.getMark());
        }

    }

    private void saveQuestion() {
        if (mQAPublishBean != null) {
            mQAPublishBean.setBody(getContentString());
            mQAPublishBean.setAnonymity(mAnonymity);
            mPresenter.saveQuestion(mQAPublishBean);
        }

    }

    @NonNull
    protected String getContentString() {
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
    protected void initData() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();

        mQAPublishBean = getArguments().getParcelable(BUNDLE_PUBLISHQA_BEAN);

        QAPublishBean draft = mPresenter.getDraftQuestion(mQAPublishBean.getMark());
        if (draft != null) {
            String body = draft.getBody();
            if (!TextUtils.isEmpty(body) && mRicheTest != null) {
                mRicheTest.getAllLayout().post(() -> {
                    mRicheTest.clearAllLayout();
                    mPresenter.pareseBody(body);
                });

            }
        }
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
        mToolbarRight.setClickable(false);
        mPbImageUpload.setVisibility(View.VISIBLE);
        String path = photoList.get(0).getImgUrl();
        LogUtils.d("photo degree", "before // " + DrawableProvider.getBitmapDegree(path));
        path = DealPhotoUtils.amendRotatePhoto(path, getContext());
        LogUtils.d("photo degree", "after // path : " + path + " degree //" + DrawableProvider.getBitmapDegree(path));
        mPresenter.uploadPic(path, "", true, 0, 0);
        mPicTag++;
        test = mRicheTest.insertImage(path, mRicheTest.getWidth());
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
    public void uploadPicSuccess(int id) {
        if (mPbImageUpload == null) {
            return;
        }
        mPbImageUpload.setVisibility(View.GONE);
        mToolbarRight.setClickable(true);
        test.setId(id);
    }

    @Override
    public void uploadPicFailed() {
        mPbImageUpload.setVisibility(View.GONE);
        mToolbarRight.setClickable(true);
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        if (prompt == Prompt.ERROR) {
            mRicheTest.deleteImage();
        }
    }

    @Override
    public void onImageDelete() {
        if (mPicTag > 0) {
            mPicTag--;
        }
    }

    @Override
    public void onContentChange(boolean hasContent) {
        mToolbarRight.setEnabled(hasContent);
    }

    @Override
    public void onPareseBodyEnd(boolean hasContent) {
        mToolbarRight.setEnabled(hasContent);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_publish_content_test;
    }

    @OnClick({R.id.im_arrowc, R.id.im_pic, R.id.im_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_arrowc:
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

    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {
        mRicheTest.hideKeyBoard();
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

        RxView.clicks(mImArrowc)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mRicheTest.hideKeyBoard());

        RxView.clicks(mImSetting)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> initAnonymityPopWindow(R.string.qa_publish_enable_anonymous));

        mPbImageUpload.setOnTouchListener((v, event) -> true);

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
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mInstructionsPopupWindow.hide())
                .build();
        mInstructionsPopupWindow.show();
    }

    private void initAnonymityPopWindow(int strRes) {
        DeviceUtils.hideSoftKeyboard(getContext(), mImSetting);

        Observable.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (mImSetting == null) {
                        return;
                    }
                    mImSetting.setImageResource(R.mipmap.icon_install_blue);
                    if (mAnonymityPopWindow != null) {
                        mAnonymityPopWindow.showParentViewTop();
                        return;
                    }
                    mAnonymityPopWindow = AnonymityPopWindow.builder()
                            .with(getActivity())
                            .isWrap(true)
                            .isFocus(true)
                            .isOutsideTouch(true)
                            .parentView(mRlPublishTool)
                            .buildDescrStr(getString(strRes))
                            .contentView(R.layout.pop_for_anonymity)
                            .backgroundAlpha(POPUPWINDOW_ALPHA)
                            .buildAnonymityPopWindowSwitchClickListener(isChecked -> initAnonymityAlertPopWindow(isChecked))
                            .build();
                    if (mAnonymity == 1 || mQAPublishBean != null && mQAPublishBean.getAnonymity() == 1) {
                        mAnonymityPopWindow.setSwitchButton(true);
                    }
                    mAnonymityPopWindow.showParentViewTop();
                    mAnonymityPopWindow.setOnDismissListener(() -> mImSetting.setImageResource(R.mipmap.icon_install_grey));
                }, Throwable::printStackTrace);

    }

    private void initAnonymityAlertPopWindow(boolean isChecked) {
        if (mAnonymityAlertPopWindow == null) {
            mAnonymityAlertPopWindow = CenterAlertPopWindow.builder()
                    .with(getActivity())
                    .parentView(getView())
                    .isOutsideTouch(false)
                    .isFocus(false)
                    .animationStyle(R.style.style_actionPopupAnimation)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .titleStr(getString(R.string.qa_publish_enable_anonymous))
                    .desStr("当我回忆你过去做过的某些事情的时候，要接受你就变得相当困难，不过最终我仍旧理解了你。" +
                            "我始终在期望你做一些你的选择，期望你做一些你无法做到的事情，这种期望使我对你过于吹毛求疵，" +
                            "当初的你，怎么会像现在的我一样成熟呢！如今我终于接受了你的存在，你绝对值得。")
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
                            if (mAnonymityPopWindow != null) {
                                mAnonymityPopWindow.dismiss();
                            }
                        }

                        @Override
                        public void onLeftClicked() {
                            mAnonymityAlertPopWindow.dismiss();
                            mAnonymity = 0;
                            if (mAnonymityPopWindow != null) {
                                // 设置按钮的状态
                                mAnonymityPopWindow.setSwitchButton(false);
                                mAnonymityPopWindow.dismiss();
                            }
                        }
                    })
                    .build();
        }
        if (isChecked && (mQAPublishBean == null || mQAPublishBean.getAnonymity() != 1) && showAnonymityAlertPopWindow()) {
            mAnonymityAlertPopWindow.show();
        } else {
            mAnonymityAlertPopWindow.dismiss();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_PUBLISH_QUESTION)
    public void onPublishQuestionSuccess(Bundle bundle) {
        // 发布成功后关闭这个页面
        getActivity().finish();
    }

    protected boolean showAnonymityAlertPopWindow() {
        return true;
    }

}
