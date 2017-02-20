package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhiyicx.common.base.BaseApplication;

public class MusicPlayService extends Service {

    private String url = "http://hd.xiaotimi.com/2016/myxc/ok1/GKL.mp4?#.mp3";
    private OnPrepareMusiListener mPrepareMusiListener;
    private MyAudioPlayer mAudioPlayer;
    public MUsicBinder mBinder = new MUsicBinder();

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void playAudiofromSDCard(String path) {
        mAudioPlayer.PrepareAudiofromSDCard(path);
    }

    public void PrepareAudiofromInternet(String url, boolean isAsync) {
        mAudioPlayer.PrepareAudiofromInternet(url, isAsync);
    }

    public void playOrPause() {
        mAudioPlayer.startOrpause();
    }

    public boolean isPlaying() {
        return mAudioPlayer.isPlaying();
    }

    public void stop() {
        mAudioPlayer.stop();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPrepareMusiListener(OnPrepareMusiListener prepareMusiListener) {
        mPrepareMusiListener = prepareMusiListener;
    }

    public class MUsicBinder extends Binder {
        MusicPlayService getService(SeekBar seekBar, TextView curTime, TextView totalTime) {
            if (mAudioPlayer==null){
                mAudioPlayer = new MyAudioPlayer(seekBar, curTime, totalTime, BaseApplication
                        .getContext());
                mAudioPlayer.setOnPlayerStatusChangeLitener(new AudioPrepare());
            }
            return MusicPlayService.this;
        }
    }

    public interface OnPrepareMusiListener {
        void OnPrepareed();
        void OnFailure();
        void OnPlayeronComplet();
    }

    class AudioPrepare implements MyAudioPlayer.OnAudioPlayerStatusChangeLitener {
        @Override
        public void onPlayeronCompletion() {
            if (mPrepareMusiListener != null) {
                mPrepareMusiListener.OnPlayeronComplet();
            }
        }

        @Override
        public void onPlayerPrepared() {
            if (mPrepareMusiListener != null) {
                mPrepareMusiListener.OnPrepareed();
            }
        }

        @Override
        public void onFailure() {
            mPrepareMusiListener.OnFailure();
        }
    }

}

