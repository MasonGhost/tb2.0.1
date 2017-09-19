package com.zhiyicx.zhibosdk.manage.soupport;

import android.opengl.GLSurfaceView;
import android.view.ViewGroup;

import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.zhiyicx.zhibosdk.manage.listener.OnCloseStatusListener;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;
import com.zhiyicx.zhibosdk.manage.listener.OnLiveStartPlayListener;
import com.zhiyicx.zhibosdk.manage.listener.ZBFrameCapturedCallback;
import com.zhiyicx.zhibosdk.widget.ZBAspectFrameLayout;

import org.json.JSONException;

import java.io.File;

/**
 * Created by jungle on 16/7/7.
 * com.zhiyicx.zhibosdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public interface StreamingSoupport extends ChatRoomIMSupport {

    void initConfig( ZBAspectFrameLayout aspectFrameLayout, GLSurfaceView gLSurfaceView,WatermarkSetting watermarkSetting) throws JSONException, IllegalAccessException;

    /**
     * 手动使用重连策略重连
     */
    void reconnect();


    /**
     * 转换摄像头
     */
    boolean switchCamera();

    /**
     * 强制显示前置摄像头
     */
    void showCameraFacingFront();
    /**
     * 开始推流播放
     *
     * @param title       直播间名称
     * @param mLocation   直播间地址，经纬度用","隔开
     * @param mfile        直播间封面
     * @return 推流是否成功
     */
    void startPlay(String title, String mLocation, File mfile, OnLiveStartPlayListener mOnLiveStartPlayListener);

    void closePlay(OnCloseStatusListener l);

    /**
     * 禁言
     * @param usid
     * @param time   0为永久禁言，大于零则为禁言分钟数
     * @param l
     */
    void imDisable(String usid,int time,OnCommonCallbackListener l);
    /**
     * 解除禁言
     * @param usid
     * @param l
     */
    void imEnable(String usid,OnCommonCallbackListener l);

    /**
     * 停止推流
     * @return
     */
    boolean stopStreaming();

    void setNativeLoggingEnabled(boolean isEnable);

    /**
     * 在调用 captureFrame 的时候，您需要传入 width 和 height，
     * 以及 FrameCapturedCallback，如果传入的 width 或者 height 小于等于 0，
     * SDK 返回的 Bitmap 将会是预览的尺寸 。SDK 完成截帧之后，会回调 onFrameCaptured，
     * 并将结果以参数的形式返回给调用者
     *
     * @param with
     * @param height
     * @param frameCapturedCallback
     */
    void captureFrame(int with, int height, ZBFrameCapturedCallback frameCapturedCallback);


    /**
     * 是否静音
     *
     * @param isMute
     */
    void mute(boolean isMute);


    /**
     * 手动对焦
     * @param v 手动对焦的触碰布局
     * @param res 对焦时显示的视图，位于v之中
     */
    void setFocusAreaIndicator(ViewGroup v, int res);

    /**
     * 对焦坐标
     * @param x
     * @param y
     */
    void doSingleTapUp(int x, int y);


    /**
     * 设置当前zoom值
     * @param mCurrentZoom
     */
    void setZoomValue(int mCurrentZoom);

    /**
     * 获取当前zoom值
     * @return
     */
    int getZoom();

    /**
     * 获取支持的最大zoom值
     * @return
     */
    int getMaxZoom();

    /**
     * 是否支持zoom
     * @return
     */
    boolean isZoomSupported();


    /**
     * 打开闪光灯
     */
    boolean turnLightOn();

    /**
     * 关闭闪光灯
     */
    boolean turnLightOff();

    void onResume();


    void onPause();

    void onDestroy();

}
