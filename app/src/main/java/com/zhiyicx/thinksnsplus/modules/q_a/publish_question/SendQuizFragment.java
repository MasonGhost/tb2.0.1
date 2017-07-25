package com.zhiyicx.thinksnsplus.modules.q_a.publish_question;

import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.widget.MarkDownEditor;

import java.util.List;

import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.MAX_DEFAULT_COUNT;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public class SendQuizFragment extends TSFragment<SendQuizContract.Presenter> implements SendQuizContract.View,
        MarkDownEditor.OnImageSelectListener, PhotoSelectorImpl.IPhotoBackListener {

    private PhotoSelectorImpl mPhotoSelector;

    @Override
    public List<ImageBean> getImageList() {
        return null;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        initPhotoSelector();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_send_quiz;
    }

    @Override
    public void onImageSelected(List<ImageBean> photoList) {

    }

    @Override
    public void onInsertImageClick() {
        mPhotoSelector.getPhotoListFromSelector(MAX_DEFAULT_COUNT, null);
    }

    /**
     * 初始化图片选择器
     */
    private void initPhotoSelector() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
        Glide.with(getContext()).load("");
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        // 上传图片

    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片选择器返回结果
        if (mPhotoSelector != null) {
            mPhotoSelector.onActivityResult(requestCode, resultCode, data);
        }
    }
}
