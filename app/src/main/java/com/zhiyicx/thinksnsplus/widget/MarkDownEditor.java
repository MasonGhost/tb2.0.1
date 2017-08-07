package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;

import jp.wasabeef.richeditor.RichEditor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe 可以生成MarkDown的图文编辑器
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public class MarkDownEditor extends LinearLayout implements PhotoSelectorImpl.IPhotoBackListener {

    private RichEditor mEditor;
    private Button mInsertImg; // 插入图片
    private Button mBtnSetBold; // 粗体
    private BaseFragment fragment;
    private PhotoSelectorImpl mPhotoSelector;
    private OnImageSelectListener mListener;

    public MarkDownEditor(Context context) {
        super(context);
        init();
    }

    public MarkDownEditor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarkDownEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_mark_down_editor, this);
        mEditor = (RichEditor) findViewById(R.id.editor);
        mInsertImg = (Button) findViewById(R.id.btn_insert_img);
        mBtnSetBold = (Button) findViewById(R.id.btn_set_bold);
        initPhotoSelector();
        setListener();
    }

    private void setListener() {
        RxView.clicks(mInsertImg)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    // 弹出选择框 图片和相册
                    if (mListener != null){
                        mListener.onInsertImageClick();
                    }
                });
        RxView.clicks(mBtnSetBold)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> mEditor.setBold());
    }

    /**
     * 初始化图片选择器
     */
    private void initPhotoSelector() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, fragment, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
        Glide.with(fragment.getContext()).load("");
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        // 记录选中的图片，对外提供选中的图片，即时上传选中的图片
        if (mListener != null && photoList != null && !photoList.isEmpty()) {
            mListener.onImageSelected(photoList);
        }
    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        ToastUtils.showToast(errorMsg);
    }

    /**
     * 对外提供的追加图片的方法
     * @param photoList 图片列表
     */
    public void addImageList(List<ImageBean> photoList) {
        if (photoList != null && !photoList.isEmpty()) {
            for (ImageBean imageBean : photoList) {
                mEditor.insertImage(imageBean.getImgUrl(), String.valueOf(imageBean.getFeed_id()));
            }
        }
    }

    public interface OnImageSelectListener {
        void onImageSelected(List<ImageBean> photoList);
        void onInsertImageClick();
    }

    public void setOnImageSelectListener(OnImageSelectListener listener) {
        this.mListener = listener;
    }
}
