package com.zhiyicx.zhibolibrary.ui.view;

import android.graphics.Bitmap;

import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;


/**
 * Created by jess on 16/6/18.
 */
public interface CameraView extends BaseView {

    ZBLBaseFragment getFragment();

    void setCaptureBitmap(Bitmap bm);

    /**
     * 显示用去选择是否保存照片的组件
     */
    void showAction();

    /**
     *  隐藏用去选择是否保存照片的组件
     */
    void hideAction();

    /**
     * 开始剪切
     */
    void beginCrop();

    /**
     * 清除截帧图片
     */
    void clearImage();
}
