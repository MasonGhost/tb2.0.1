package com.zhiyicx.zhibosdk.widget;

import android.view.SurfaceHolder;

import com.pili.pldroid.player.PLMediaPlayer;

/**
 * Created by jungle on 16/7/7.
 * com.zhiyicx.zhibosdk.widget
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBMediaPlayer extends PLMediaPlayer {

    public interface OnErrorListener {
        boolean onError(int errorCode);
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged( int width, int height);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete();
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(int percent);
    }

    public interface OnCompletionListener {
        void onCompletion();
    }

    public interface OnPreparedListener {
        void onPrepared();
    }

    public interface OnInfoListener {
        boolean onInfo( int what, int extra);
    }

    @Override
    public void setVolume(float left, float right) {
        super.setVolume(left, right);
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
    }

    @Override
    public void seekTo(long l) throws IllegalStateException {
        super.seekTo(l);
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        super.setDisplay(surfaceHolder);
    }

    @Override
    public void setLooping(boolean b) {
        super.setLooping(b);
    }

}
