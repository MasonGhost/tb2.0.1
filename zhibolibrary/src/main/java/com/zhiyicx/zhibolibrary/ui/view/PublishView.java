package com.zhiyicx.zhibolibrary.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.GLSurfaceView;

import com.jess.camerafilters.base.FilterManager;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;
import com.zhiyicx.zhibosdk.widget.ZBAspectFrameLayout;

/**
 * Created by zhiyicx on 2016/3/23.
 */
public interface PublishView extends BaseView {
    /**
     * 显示加载
     */
    void showLoadding(boolean isSmall);

    /**
     * 隐藏加载
     */
    void hideLoadding(boolean isSmall);

    /**
     * 跳转到直播页面
     */
    void launchLivaActivity();


    /**
     * 设置button是否可以点击
     */
    void setButtonEnable(boolean isEnable);

    /**
     * 设置Button的文字
     */
    void setButtonText(String text);


    /**
     * 打开摄像头
     */
    void launchCamera();

    /**
     * 获得标题
     */
    String getTitel();

    /**
     * 设置封面图片
     */
    void setCoverPhoto(Drawable drawable);

    /**
     * 跳转到结束直播间页面
     */
    void launchEndStreamActivity(Intent intent);

    /**
     * 获得surfaceview的父布局
     *
     * @return
     */
    ZBAspectFrameLayout getCameraPreViewAFL();

    /**
     * 获得surfaceview
     *
     * @return
     */
    GLSurfaceView getCameraPreViewSFV();

    /**
     * 清除屏幕所有按键展示直播
     */
    void removeALL();

    /**
     * 恢复所有
     */
    void restoreAll();

    /**
     * 现实核心页面
     */
    void showCore();

    /**
     * 设置提示消息
     */
    void setWarnMessage(String warn);

    /**
     * 提示信息
     */
    void showWarn();

    /**
     * 隐藏提示信息
     */
    void hideWarn();

    /**
     * 获取用于在结束页面显示金币赞个数的实体
     */
    ZBEndStreamJson getEndStream();


    /**
     * 显示摄像机
     */
    void showCamera();

    /**
     * 拍照
     */
    void captureFrame();

    /**
     * 设置设计拍摄的图片
     *
     * @param bm
     */
    void setCaptureBitmap(Bitmap bm);

    /**
     * 设置断线加载的图片
     *
     * @param bm
     */
    void setLoadingCaptureBitap(Bitmap bm);

    void closeCameraFragment();

    void openCameraFragment();

    /**
     * 转换前置和后置摄像头
     */
    void switchCamera();

    /**
     * 发送到剪切页面
     */
    void beginCrop(Uri source);

    /**
     * 打开相册
     */
    void launchAlbum();

    /**
     * 清除相机fragment的图片
     */
    void clearCameraFragmentImg();

    /**
     * 是否显示加载动画
     *
     * @param isVisiable
     */
    void setPlaceHolderVisible(boolean isVisiable);

    /**
     * 设置是否可以选择封面
     */
    void setSelectEnable(boolean isEnable);

    void isSelfClose(boolean isClose);

    FilterManager getFilterManager();

    Activity getActivity();
}
