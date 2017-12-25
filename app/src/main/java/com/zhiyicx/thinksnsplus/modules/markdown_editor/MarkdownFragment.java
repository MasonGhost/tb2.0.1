package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyi.richtexteditorlib.SimpleRichEditor;
import com.zhiyi.richtexteditorlib.base.RichEditor;
import com.zhiyi.richtexteditorlib.view.BottomMenu;
import com.zhiyi.richtexteditorlib.view.dialogs.LinkDialog;
import com.zhiyi.richtexteditorlib.view.dialogs.PictureHandleDialog;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.publish.choose_circle.ChooseCircleActivity;
import com.zhiyicx.thinksnsplus.modules.circle.publish.choose_circle.ChooseCircleFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @author Jliuer
 * @Date 2017/11/17/13:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MarkdownFragment extends TSFragment<MarkdownContract.Presenter> implements
        SimpleRichEditor.OnEditorClickListener, PhotoSelectorImpl.IPhotoBackListener,
        MarkdownContract.View, RichEditor.OnMarkdownWordResultListener {

    public static final String BUNDLE_SOURCE_DATA = "sourceId";

    @BindView(R.id.lu_bottom_menu)
    protected BottomMenu mBottomMenu;
    @BindView(R.id.rich_text_view)
    protected SimpleRichEditor mRichTextView;
    @BindView(R.id.ll_circle_container)
    protected LinearLayout mLlCircleContainer;
    @BindView(R.id.line)
    protected View mLine;
    @BindView(R.id.cb_syn_to_dynamic)
    protected CheckBox mCbSynToDynamic;
    @BindView(R.id.tv_name)
    protected TextView mCircleName;

    /**
     * 记录上传成功的照片 键值对：时间戳(唯一) -- 图片地址
     */
    protected HashMap<Long, String> mInsertedImages;

    /**
     * 记录上传失败的照片 同上
     */
    protected HashMap<Long, String> mFailedImages;

    /**
     * 上传图片成功后返回的id
     */
    protected List<Integer> mImages;

    protected PhotoSelectorImpl mPhotoSelector;

    /**
     * 图片选择弹出
     */
    protected ActionPopupWindow mPhotoPopupWindow;

    /**
     * t保存草稿提示
     */
    protected ActionPopupWindow mEditWarningPopupWindow;

    /**
     * 标题+文字 长度
     */
    protected int mContentLength;

    /**
     * 发布资源之前的处理，比如封装数据
     *
     * @return 数据是否完整
     */
    protected boolean preHandlePublish() {
        return false;
    }

    /**
     * 发布
     *
     * @param title      标题
     * @param markdwon   内容（含有格式）
     * @param noMarkdown 内容（不含格式）
     */
    protected void handlePublish(String title, String markdwon, String noMarkdown) {
    }

    /**
     * 初始化 传递过来的 参数
     */
    protected void initBundleDataWhenOnCreate() {
    }

    /**
     * 圈外发表帖子 选择圈子的回掉
     *
     * @param circleInfo
     */
    protected void onActivityResultForChooseCircle(CircleInfo circleInfo) {

    }

    /**
     * 在这里初始化 编辑器
     */
    protected void editorPreLoad() {
        BaseDraftBean draft = getDraftData();
        if (draft == null) {
            mRichTextView.load();
        } else {
            loadDraft(draft);
        }
    }

    /**
     * 是否开启保存草稿
     *
     * @return
     */
    protected boolean openDraft() {
        return true;
    }

    /**
     * 还原草稿
     *
     * @param draft
     */
    protected void loadDraft(BaseDraftBean draft) {
    }

    /**
     * 草稿内容
     *
     * @return
     */
    protected BaseDraftBean getDraftData() {
        return null;
    }

    public static MarkdownFragment newInstance(Bundle bundle) {
        MarkdownFragment markdownFragment = new MarkdownFragment();
        markdownFragment.setArguments(bundle);
        return markdownFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initBundleDataWhenOnCreate();
        }
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.publish);
    }

    @Override
    protected void setLeftClick() {
//        super.setLeftClick();
        onBackPressed();
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        if (!preHandlePublish()) {
            return;
        }
        mRichTextView.getResultWords(true);
    }

    /**
     * @param title      标题
     * @param markdwon   markdown 格式内容
     * @param noMarkdown 纯文字内容
     * @param isPublish  是否是发送
     *                   如果不是发送：markdwon → 全部 html 格式内容
     */
    @Override
    public void onMarkdownWordResult(String title, String markdwon, String noMarkdown, boolean isPublish) {
        if (isPublish) {
            List<Integer> result = RegexUtils.getImageIdsFromMarkDown(MarkdownConfig.IMAGE_FORMAT, markdwon);
            if (mImages.containsAll(result)) {
                mImages.clear();
                mImages.addAll(result);
            }
            handlePublish(title, markdwon, noMarkdown);
        } else {
            boolean canSaveDraft = !TextUtils.isEmpty(title + noMarkdown);
            if (!canSaveDraft || !openDraft()) {
                mActivity.finish();
                return;
            }
            initEditWarningPop(title, markdwon, noMarkdown);
            DeviceUtils.hideSoftKeyboard(mActivity.getApplication(), mRichTextView);
        }
    }

    @Override
    protected void initView(View rootView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            AndroidBug5497Workaround.assistActivity(getActivity());
        }
        mToolbarRight.setEnabled(false);
        mInsertedImages = new HashMap<>();
        mFailedImages = new HashMap<>();
        mImages = new ArrayList<>();
        initListener();
        editorPreLoad();
    }

    @Override
    protected void initData() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
    }

    @Override
    public void onBackPressed() {
        mRichTextView.getResultWords(false);
    }

    protected void initListener() {
        mRichTextView.setOnEditorClickListener(this);
        mRichTextView.setOnTextLengthChangeListener(length -> {

        });
        mRichTextView.setOnMarkdownWordResultListener(this);
        mRichTextView.setBottomMenu(mBottomMenu);

        mLlCircleContainer.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, ChooseCircleActivity.class);
            mActivity.startActivityForResult(intent, ChooseCircleFragment.CHOOSE_CIRCLE);
        });
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.publish_post);
    }

    @Override
    public void onLinkButtonClick() {
        showLinkDialog(LinkDialog.createLinkDialog(), false);
    }

    @Override
    public void onInsertImageButtonClick() {
        DeviceUtils.hideSoftKeyboard(mActivity.getApplication(), mRichTextView);
        initPhotoPopupWindow();
    }

    @Override
    public void onLinkClick(String name, String url) {
        showLinkDialog(LinkDialog.createLinkDialog(name, url), true);
    }

    @Override
    public void onImageClick(Long id) {
//        if (mInsertedImages.containsKey(id)) {
//            showPictureClickDialog(PictureHandleDialog.createDeleteDialog(id), new
//                    CharSequence[]{getString(R.string.delete)});
//        } else if (mFailedImages.containsKey(id)) {
//            showPictureClickDialog(PictureHandleDialog.createDeleteDialog(id),
//                    new CharSequence[]{getString(R.string.delete), getString(R.string.retry)});
//        }
    }

    @Override
    public void onTextStypeClick(boolean isSelect) {
        setSynToDynamicCbVisiable(!isSelect);
    }

    @Override
    public void onInputListener(int length) {
        mContentLength = length;
        setRightClickable(length > 0);
    }

    protected void setRightClickable(boolean clickable) {
        mToolbarRight.setEnabled(clickable);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_markd_down;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooseCircleFragment.CHOOSE_CIRCLE) {
            if (data != null && data.getExtras().getParcelable(ChooseCircleFragment.BUNDLE_CIRCLE) != null) {
                onActivityResultForChooseCircle(data.getExtras().getParcelable(ChooseCircleFragment.BUNDLE_CIRCLE));
            }
        } else {
            mPhotoSelector.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (photoList.isEmpty()) {
            return;
        }
        String path = photoList.get(0).getImgUrl();
        long id = SystemClock.currentThreadTimeMillis();
        long size[] = ImageUtils.getBitmapSize(path);
        mRichTextView.insertImage(path, id, size[0], size[1]);
        mInsertedImages.put(id, path);
        mPresenter.uploadPic(path, id);
    }

    @Override
    public void onUploading(long id, String filePath, int progress, int imgeId) {
        getActivity().runOnUiThread(() -> {
            if (progress == 100) {
                mImages.add(imgeId);
            }
            mRichTextView.setImageUploadProcess(id, progress, imgeId);
        });
    }

    @Override
    public void sendPostSuccess(CirclePostListBean data) {
        CirclePostDetailActivity.startActivity(getActivity(), data.getGroup_id(), data.getId(),
                false);
        getActivity().finish();
    }

    @Override
    public void onFailed(String filePath, long id) {
        getActivity().runOnUiThread(() -> {
            mRichTextView.setImageFailed(id);
            mInsertedImages.remove(id);
            mFailedImages.put(id, filePath);
        });
    }

    /**
     * @param isVisiable true  显示
     */
    protected void setSynToDynamicCbVisiable(boolean isVisiable) {

    }

    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {

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
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
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

    protected void initEditWarningPop(String title, String html, String noMarkdown) {
        if (mEditWarningPopupWindow != null) {
            mEditWarningPopupWindow.show();
            return;
        }
        mEditWarningPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.edit_quit))
                .item2Str(getString(canSaveDraft() ? R.string.save_to_draft_box : R.string.empty))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    cancleEdit();
                    mEditWarningPopupWindow.hide();
                    getActivity().finish();
                })
                .item2ClickListener(() -> {
                    saveDraft(title, html, noMarkdown);
                    mEditWarningPopupWindow.hide();
                    getActivity().finish();
                })
                .bottomClickListener(() -> mEditWarningPopupWindow.hide())
                .build();
        mEditWarningPopupWindow.show();
    }

    protected boolean canSaveDraft() {
        return true;
    }

    protected void saveDraft(String title, String html, String noMarkdown) {
    }

    protected void cancleEdit() {

    }

    protected void showLinkDialog(final LinkDialog dialog, final boolean isChange) {
        dialog.setListener(new LinkDialog.OnDialogClickListener() {
            @Override
            public void onConfirmButtonClick(String name, String url) {
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url)) {
                    ToastUtils.showToast(R.string.not_empty);
                } else {
                    if (!isChange) {
                        mRichTextView.insertLink(url, name);
                    } else {
                        mRichTextView.changeLink(url, name);
                    }
                    onCancelButtonClick();
                }
            }

            @Override
            public void onCancelButtonClick() {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), LinkDialog.Tag);
    }

    protected void showPictureClickDialog(final PictureHandleDialog dialog, CharSequence[] items) {

        dialog.setListener(new PictureHandleDialog.OnDialogClickListener() {
            @Override
            public void onDeleteButtonClick(Long id) {
                mRichTextView.deleteImageById(id);
                removeFromLocalCache(id);
            }

            @Override
            public void onReloadButtonClick(Long id) {
                mRichTextView.setImageReload(id);
                mPresenter.uploadPic(mFailedImages.get(id), id);
                mInsertedImages.put(id, mFailedImages.get(id));
                mFailedImages.remove(id);
            }
        });
        dialog.setItems(items);
        dialog.show(getFragmentManager(), PictureHandleDialog.Tag);
    }

    protected void removeFromLocalCache(long id) {
        if (mInsertedImages.containsKey(id)) {
            mInsertedImages.remove(id);
        } else if (mFailedImages.containsKey(id)) {
            mFailedImages.remove(id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPop(mPhotoPopupWindow);
        dismissPop(mEditWarningPopupWindow);
    }
}
