package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicPlayService extends Service {

    public MediaPlayer mediaPlayer;
    private String url="http://hd.xiaotimi.com/2016/myxc/ok1/GKL.mp4?#.mp3";

    public MusicPlayService() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  通过 Binder 来保持 Activity 和 Service 的通信
    public MyBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

    public void playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

