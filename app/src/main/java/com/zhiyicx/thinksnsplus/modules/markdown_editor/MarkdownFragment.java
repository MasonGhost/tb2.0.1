package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import android.view.View;

import com.lu.richtexteditorlib.SimpleRichEditor;
import com.lu.richtexteditorlib.base.RichEditor;
import com.lu.richtexteditorlib.view.LuBottomMenu;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.thinksnsplus.R;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/11/17/13:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MarkdownFragment extends TSFragment implements SimpleRichEditor.OnEditorClickListener, View.OnClickListener, PhotoSelectorImpl.IPhotoBackListener {

    @BindView(R.id.lu_bottom_menu)
    private LuBottomMenu mLuBottomMenu;
    @BindView(R.id.rich_text_view)
    private SimpleRichEditor mRichTextView;

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
        mRichTextView.setOnTextLengthChangeListener(new RichEditor.OnTextLengthChangeListener() {
            @Override
            public void onTextLengthChange(final long length) {

            }
        });
        mRichTextView.setLuBottomMenu(mLuBottomMenu);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLinkButtonClick() {

    }

    @Override
    public void onInsertImageButtonClick() {

    }

    @Override
    public void onLinkClick(String name, String url) {

    }

    @Override
    public void onImageClick(Long id) {

    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {

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
}
