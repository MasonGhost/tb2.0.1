package com.zhiyicx.zhibosdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.zhiyicx.zhibosdk.widget.soupport.VideoViewSoupport;

/**
 * Created by jungle on 16/7/7.
 * com.zhiyicx.zhibosdk
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBTextureView extends PLVideoTextureView implements VideoViewSoupport, PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener, PLMediaPlayer.OnSeekCompleteListener {

    private ZBMediaPlayer.OnPreparedListener mOnPreparedListener;
    private ZBMediaPlayer.OnInfoListener mOnInfoListener;
    private ZBMediaPlayer.OnCompletionListener mOnCompletionListener;

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

    private ZBMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private ZBMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private ZBMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private ZBMediaPlayer.OnErrorListener mOnErrorListener;


    public ZBTextureView(Context context) {
        super(context);
        init();
    }

    public ZBTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ZBTextureView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    public ZBTextureView(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init();
    }


    @Override
    public void init() {


        setDisplayAspectRatio(ASPECT_RATIO_PAVED_PARENT);
        initListener();
    }

    private void initListener() {
        setOnPreparedListener(this);
        setOnInfoListener(this);
        setOnCompletionListener(this);
        setOnVideoSizeChangedListener(this);
        setOnErrorListener(this);
        setOnSeekCompleteListener(this);

    }

    @Override
    public void setAVOptions(AVOptions avOptions) {
        super.setAVOptions(avOptions);
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

    /**
     * 持播放画面以 0度，90度，180度，270度旋转
     *
     * @param i
     * @return
     */
    @Override
    public boolean setDisplayOrientation(int i) {
        return super.setDisplayOrientation(i);
    }

    /**
     * 设置画面镜像变化
     *
     * @param b
     */
    @Override
    public void setMirror(boolean b) {
        super.setMirror(b);
    }

    @Override
    public void setVideoPath(String path) {

        setAVOptions(ZBVideoView.getAVOptions(path));
        super.setVideoPath(path);
    }


    /**
     * 调用在setVideoPath之后
     *不需要调用start,准备好自动播放
     * @param b
     */
    @Override
    public void isNeedStartOnPrepared(boolean b,String path) {
        AVOptions avOptions = ZBVideoView.getAVOptions(path);
        if (b)
            avOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 1);

        else
            avOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
        setAVOptions(avOptions);
        super. setVideoPath(path);
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
