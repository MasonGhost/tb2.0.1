package com.zhiyicx.zhibosdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.zhiyicx.zhibosdk.widget.soupport.VideoViewSoupport;

/**
 * Created by jungle on 16/7/7.
 * com.zhiyicx.zhibosdk
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBVideoView extends PLVideoView implements VideoViewSoupport, PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener,
        PLMediaPlayer.OnSeekCompleteListener,
        PLMediaPlayer.OnBufferingUpdateListener {

    public void setOnPreparedListener(ZBMediaPlayer.OnPreparedListener onPreparedListener) {
        mOnPreparedListener = onPreparedListener;
    }

    public void setOnInfoListener(ZBMediaPlayer.OnInfoListener onInfoListener) {
        mOnInfoListener = onInfoListener;
    }

    public void setOnCompletionListener(ZBMediaPlayer.OnCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }

    public void setOnBufferingUpdateListener(ZBMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener) {
        mOnBufferingUpdateListener = onBufferingUpdateListener;
    }

    public void setOnSeekCompleteListener(ZBMediaPlayer.OnSeekCompleteListener onSeekCompleteListener) {
        mOnSeekCompleteListener = onSeekCompleteListener;
    }

    public void setOnVideoSizeChangedListener(ZBMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        mOnVideoSizeChangedListener = onVideoSizeChangedListener;
    }

    public void setOnErrorListener(ZBMediaPlayer.OnErrorListener onErrorListener) {
        mOnErrorListener = onErrorListener;
    }

    private ZBMediaPlayer.OnPreparedListener mOnPreparedListener;
    private ZBMediaPlayer.OnInfoListener mOnInfoListener;
    private ZBMediaPlayer.OnCompletionListener mOnCompletionListener;
    private ZBMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private ZBMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private ZBMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private ZBMediaPlayer.OnErrorListener mOnErrorListener;


    public ZBVideoView(Context context) {
        super(context);
        init();
    }

    public ZBVideoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ZBVideoView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    public ZBVideoView(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init();
    }


    @Override
    public void init() {
        setDisplayAspectRatio(ASPECT_RATIO_PAVED_PARENT);
        initListener();
    }

    public static AVOptions getAVOptions(String path) {

        AVOptions options = new AVOptions();

        if (isLiveStreaming(path)) {
            // 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
            // 默认值是：无
            options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

            // 读取视频流超时时间，单位是 ms
            // 默认值是：10 * 1000
            options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
            // 当前播放的是否为在线直播，如果是，则底层会有一些播放优化
            // 默认值是：0
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        }

        // 解码方式，codec＝1，硬解; codec=0, 软解
        // 默认值是：0
        options.setInteger(AVOptions.KEY_MEDIACODEC, 1);

        // 是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`
        // 默认值是：1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);


        return options;
    }

    public static boolean isLiveStreaming(String url) {
        if (url.startsWith("rtmp://")
                || (url.startsWith("http://") && url.endsWith(".m3u8"))
                || (url.startsWith("http://") && url.endsWith(".flv"))) {
            return true;
        }
        return false;
    }

    private void initListener() {
        setOnPreparedListener(this);
        setOnInfoListener(this);
        setOnCompletionListener(this);
        setOnVideoSizeChangedListener(this);
        setOnErrorListener(this);
        setOnSeekCompleteListener(this);
        setOnBufferingUpdateListener(this);
    }


    /**
     * 画面预览模式，包括：原始尺寸、适应屏幕、全屏铺满、16:9、4:3 等
     */
    @Override
    public void setDisplayAspectRatio(int ratio) {
        super.setDisplayAspectRatio(ratio);
    }


    /**
     * 设置加载动画的接口，在播放器进入缓冲状态时，自动显示加载界面，缓冲结束后，自动隐藏加载界面
     *
     * @param view
     */
    @Override
    public void setBufferingIndicator(View view) {
        super.setBufferingIndicator(view);
    }


    @Override
    public void setMediaController(IMediaController iMediaController) {
        super.setMediaController(iMediaController);
    }


    @Override
    public void setVideoPath(String path) {
        setAVOptions(getAVOptions(path));
        super.setVideoPath(path);
    }


    /**
     * 调用在setVideoPath之后
     * 不需要调用start,准备好自动播放
     *
     * @param b
     */
    @Override
    public void isNeedStartOnPrepared(boolean b, String path) {
        AVOptions avOptions = getAVOptions(path);
        if (b)
            avOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 1);

        else
            avOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
        setAVOptions(avOptions);
        super.setVideoPath(path);
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        if (mOnCompletionListener != null)
            mOnCompletionListener.onCompletion();
    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        if (mOnErrorListener != null) {
            return mOnErrorListener.onError(i);
        }
        return false;
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int i, int i1) {
        if (mOnInfoListener != null) {
            return mOnInfoListener.onInfo(i, i1);
        }
        return false;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer) {
        if (mOnPreparedListener != null)
            mOnPreparedListener.onPrepared();
    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1) {
        if (mOnVideoSizeChangedListener != null)
            mOnVideoSizeChangedListener.onVideoSizeChanged(i, i1);
    }

    @Override
    public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
        if (mOnSeekCompleteListener != null)
            mOnSeekCompleteListener.onSeekComplete();
    }

    @Override
    public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int i) {
        if (mOnBufferingUpdateListener != null) {
            mOnBufferingUpdateListener.onBufferingUpdate(i);
        }
    }
    /**
     * 是否循环播放
     *
     * @param b
     */
    @Override
    public void setLooping(boolean b) {
        super.setLooping(b);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stopPlayback() {
        super.stopPlayback();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void setVolume(float left, float right) {
        super.setVolume(left, right);
    }


}
