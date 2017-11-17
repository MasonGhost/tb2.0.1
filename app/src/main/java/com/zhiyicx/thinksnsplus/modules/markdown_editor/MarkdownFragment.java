package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;

import com.lu.richtexteditorlib.SimpleRichEditor;
import com.lu.richtexteditorlib.view.LuBottomMenu;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/11/17/13:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MarkdownFragment extends TSFragment<MarkdownContract.Presenter> implements SimpleRichEditor.OnEditorClickListener,
        View.OnClickListener, PhotoSelectorImpl.IPhotoBackListener, MarkdownContract.View {

    @BindView(R.id.lu_bottom_menu)
    LuBottomMenu mLuBottomMenu;
    @BindView(R.id.rich_text_view)
    SimpleRichEditor mRichTextView;

    private HashMap<Long, String> mInsertedImages;
    private HashMap<Long, String> mFailedImages;

    private PhotoSelectorImpl mPhotoSelector;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private ActionPopupWindow mCanclePopupWindow;// 取消提示选择弹框

    public static MarkdownFragment newInstance() {
        return new MarkdownFragment();
    }

    @Override
    protected void initView(View rootView) {
        mInsertedImages = new HashMap<>();
        mFailedImages = new HashMap<>();
        init();
        mRichTextView.load();
    }

    private void init() {
        mRichTextView.setOnEditorClickListener(this);
        mRichTextView.setOnTextLengthChangeListener(length -> {

        });
        mRichTextView.setLuBottomMenu(mLuBottomMenu);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return "哈哈哈哈";
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLinkButtonClick() {

    }

    @Override
    public void onInsertImageButtonClick() {
        initPhotoPopupWindow();
    }

    @Override
    public void onLinkClick(String name, String url) {

    }

    @Override
    public void onImageClick(Long id) {

    }

    @Override
    public void getPhotoFailure(String errorMsg) {

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
    protected int getBodyLayoutId() {
        return R.layout.fragment_markd_down;
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
        String path = photoList.get(0).getImgUrl();
        long id = SystemClock.currentThreadTimeMillis();
        long size[] = ImageUtils.getBitmapSize(path);
        mRichTextView.insertImage(path, id, size[0], size[1]);
        mInsertedImages.put(id, path);
        mPresenter.uploadPic(path, id);
    }

    @Override
    public void onUploading(long id, String filePath, int progress) {
        getActivity().runOnUiThread(() -> mRichTextView.setImageUploadProcess(id, progress));

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
                    getActivity().finish();
                })
                .bottomClickListener(() -> mCanclePopupWindow.hide()).build();
        mCanclePopupWindow.show();
    }
}
