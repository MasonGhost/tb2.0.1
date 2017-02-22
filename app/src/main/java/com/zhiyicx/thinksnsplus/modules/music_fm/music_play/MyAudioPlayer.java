package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhiyicx.common.utils.TimeUtils;

import java.io.File;
import java.io.FileDescriptor;

/**
 * @Author Jliuer
 * @Date 2017/2/17/18:16
 * @Email Jliuer@aliyun.com
 * @Description 音乐播放器
 */
public class MyAudioPlayer implements OnBufferingUpdateListener,
        OnCompletionListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private TextView curTime;
    private TextView totalTime;
    private Context mContext;
    private boolean isPrepared = false;

    private OnAudioPlayerStatusChangeLitener mPlayerLitener;

    public MyAudioPlayer(SeekBar skbProgress, TextView curTime, TextView totalTime, Context
            mContext) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        this.mSeekBar = skbProgress;
        this.mContext = mContext;
        this.curTime = curTime;
        this.totalTime = totalTime;
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {
        if (mPlayerLitener != null) {
            mPlayerLitener.onPlayerPrepared();
            mSeekBar.setMax(100);
        }
        totalTime.setText(TimeUtils.getStandardTimeWithMinAndSec((long) mMediaPlayer.getDuration
                ()));
        isPrepared = true;
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        if (mPlayerLitener != null) {
            mPlayerLitener.onPlayeronCompletion();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        mSeekBar.setSecondaryProgress(bufferingProgress);
        int duration = arg0.getDuration();
        int currentProgress = mSeekBar.getMax() * arg0.getCurrentPosition() / duration;
        Log.e(currentProgress + "% play", bufferingProgress + "% buffer");

    }

    public void PrepareAudiofromAssets(String res) {
        try {
            mediaPlayerPrepare();
            AssetFileDescriptor aFD = mContext.getAssets().openFd(res);
            FileDescriptor fileDescriptor = aFD.getFileDescriptor();
            this.mMediaPlayer.setDataSource(fileDescriptor, aFD.getStartOffset(), aFD.getLength());
            aFD.close();
            PlayAudio(false);
        } catch (Exception e) {

        }
    }

    public void PrepareAudiofromSDCard(String path) {
        try {
            mediaPlayerPrepare();
            //sdFile.getAbsoluteFile() + File.separator + "welcome.3gp"
            File sdFile = Environment.getExternalStorageDirectory();
            this.mMediaPlayer.setDataSource(path);
            PlayAudio(false);
        } catch (Exception e) {

        }
    }

    public void PrepareAudiofromInternet(String url, boolean isAsync) {
        try {
            mediaPlayerPrepare();
            this.mMediaPlayer.setDataSource(mContext, Uri.parse(url));
            PlayAudio(isAsync);
        } catch (Exception e) {

        }
    }

    public void PrepareAudiofromResource(int resID) {
        try {
            mediaPlayerPrepare();
            this.mMediaPlayer = MediaPlayer.create(mContext, resID);
            PlayAudio(false);
        } catch (Exception e) {

        }
    }

    private void mediaPlayerPrepare() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
        }else{
            mMediaPlayer.reset();
        }
    }

    private void PlayAudio(boolean isLargeRes) {
        try {
            //准备完成后才可以播放,另外如果文件特别大或者从网上获得的资源
            //会在这里等待时间过长,造成堵塞,这样的话就得用
            if (isLargeRes) {
                this.mMediaPlayer.prepareAsync();
            } else {
                this.mMediaPlayer.prepare();
            }
        } catch (Exception e) {
            if (mPlayerLitener != null) {
                mPlayerLitener.onFailure();
            }
        }
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void startOrpause() {
        if (isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            isPrepared = false;
            mSeekBar.setProgress(0);
            mSeekBar.setSecondaryProgress(0);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void reStart() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(0);
        }
    }

    public void setPlayerLitener(OnAudioPlayerStatusChangeLitener playerLitener) {
        mPlayerLitener = playerLitener;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setOnPlayerStatusChangeLitener(OnAudioPlayerStatusChangeLitener mPlayerLitener) {
        this.mPlayerLitener = mPlayerLitener;
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {

        private int progress;
        private boolean fromUser;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            this.progress = (progress / seekBar.getMax()) * mMediaPlayer.getDuration();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            fromUser = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与媒体时间的数字，而不是与seekBar.getMax()相对的数字
            if (fromUser) {
                mMediaPlayer.seekTo(progress);
            }
            fromUser = false;
        }
    }

    public interface OnAudioPlayerStatusChangeLitener {
        void onPlayeronCompletion();

        void onPlayerPrepared();

        void onFailure();
    }

}
