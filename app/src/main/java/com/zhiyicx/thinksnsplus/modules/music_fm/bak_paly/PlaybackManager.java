package com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import org.simple.eventbus.EventBus;

import java.util.Random;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_MUSIC_COMPLETE;

/**
 * @Author Jliuer
 * @Date 2017/2/21/14:34
 * @Email Jliuer@aliyun.com
 * @Description 音乐播放器管理类
 */
public class PlaybackManager implements Playback.Callback {

    /**
     * 播放模式
     */
    public static final int ORDERRANDOM = 0;
    public static final int ORDERSINGLE = 1;
    public static final int ORDERLOOP = 2;
    public static final String ORDER_ACTION = "com.zhiyicx.action.order_action";
    public static final String MUSIC_ACTION = "com.zhiyicx.action.music_action";
    public static final String MUSIC_ACTION_BUNDLE = "com.zhiyicx.action.music_action_bundle";
    public static final String MUSIC_ID = "com.zhiyicx.action.music_id";

    private QueueManager mQueueManager;

    private Playback mPlayback;

    private PlaybackServiceCallback mServiceCallback;

    private MediaSessionCallback mMediaSessionCallback;

    private int orderType = ORDERLOOP;
    private String currentMusicMediaId;

    public PlaybackManager(PlaybackServiceCallback serviceCallback,
                           QueueManager queueManager, Playback playback) {
        mServiceCallback = serviceCallback;
        mQueueManager = queueManager;
        mMediaSessionCallback = new MediaSessionCallback();
        mPlayback = playback;
        mPlayback.setCallback(this);
    }

    public void handlePlayRequest() {
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
        if (currentMusic != null) {
            currentMusicMediaId = currentMusic.getDescription().getMediaId();
            mServiceCallback.onPlaybackStart();
            mPlayback.play(currentMusic);
        }
    }

    public void handlePauseRequest() {
        if (mPlayback.isPlaying()) {
            mPlayback.pause();
            mServiceCallback.onPlaybackStop();
        }
    }

    public void handleStopRequest(String withError) {
        mPlayback.stop(true);
        mServiceCallback.onPlaybackStop();
        updatePlaybackState(withError);
    }

    public void updatePlaybackState(String error) {
        long position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
        if (mPlayback != null && mPlayback.isConnected()) {
            position = mPlayback.getCurrentStreamPosition();
        }

        //noinspection ResourceType
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(getAvailableActions());

        setCustomAction(stateBuilder);
        int state = mPlayback.getState();

        if (error != null) {
            stateBuilder.setErrorMessage(error);
            state = PlaybackStateCompat.STATE_ERROR;
        }

        //noinspection ResourceType
        stateBuilder.setState(state, position, 1.0f, SystemClock.elapsedRealtime());
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
        if (currentMusic != null) {
            stateBuilder.setActiveQueueItemId(currentMusic.getQueueId());
        }

        mServiceCallback.onPlaybackStateUpdated(stateBuilder.build());

        if (state == PlaybackStateCompat.STATE_PLAYING ||
                state == PlaybackStateCompat.STATE_PAUSED) {
            mServiceCallback.onNotificationRequired();
        }
    }

    private void setCustomAction(PlaybackStateCompat.Builder stateBuilder) {
//        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
//        if (currentMusic == null) {
//
//        }
//        String mediaId = currentMusic.getDescription().getMediaId();
//        if (mediaId == null) {
//
//        }
    }

    private long getAvailableActions() {
        long actions =
                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        if (mPlayback.isPlaying()) {
            actions |= PlaybackStateCompat.ACTION_PAUSE;
        } else {
            actions |= PlaybackStateCompat.ACTION_PLAY;
        }
        return actions;
    }

    @Override
    public void onCompletion() {
        EventBus.getDefault().post(orderType,
                EVENT_SEND_MUSIC_COMPLETE);
        switch (orderType) {
            case ORDERRANDOM:
                Random random = new Random();
                if (mQueueManager.skipQueuePosition(random.nextInt(mQueueManager.getCurrentQueueSize()))) {
                    handlePlayRequest();
                    mQueueManager.updateMetadata();
                } else {
                    handleStopRequest(null);
                }
            case ORDERLOOP:
                if (mQueueManager.skipQueuePosition(1)) {
                    handlePlayRequest();
                    mQueueManager.updateMetadata();
                } else {
                    handleStopRequest(null);
                }
                break;
            case ORDERSINGLE:
                if (mQueueManager.setCurrentQueueItem(currentMusicMediaId)) {
                    handlePlayRequest();
                    mQueueManager.updateMetadata();
                } else {
                    handleStopRequest(null);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onPlaybackStatusChanged(int state) {
        updatePlaybackState(null);
    }

    @Override
    public void onError(String error, int state) {
        if (mQueueManager.getCurrentQueueSize() == 1 || state == PlaybackStateCompat.STATE_PAUSED) {
            WindowUtils.hidePopupWindow();
            updatePlaybackState(error);
        } else {
            // 如果不止一首歌，那么久播放下一个
            EventBus.getDefault().post(orderType, EVENT_SEND_MUSIC_COMPLETE);
            if (mQueueManager.skipQueuePosition(1)) {
                handlePlayRequest();
                mQueueManager.updateMetadata();
            } else {
                handleStopRequest(null);
            }
//            onCompletion();
        }
    }

    @Override
    public void setCurrentMediaId(String mediaId) {
        mQueueManager.setQueueFromMusic(mediaId);
    }

    @Override
    public void onBuffering(int percent) {
        mServiceCallback.onBufferingUpdate(percent);
    }

    public int getState() {
        return mPlayback.getState();
    }

    public void switchToPlayback(Playback playback, boolean resumePlaying) {
        if (playback == null) {
            throw new IllegalArgumentException("Playback cannot be null");
        }
        // suspend the current one.
        int oldState = getState();
        int pos = mPlayback.getCurrentStreamPosition();
        String currentMediaId = mPlayback.getCurrentMediaId();
        mPlayback.stop(false);
        playback.setCallback(this);
        playback.setCurrentStreamPosition(pos < 0 ? 0 : pos);
        playback.setCurrentMediaId(currentMediaId);
        playback.start();
        // finally swap the instance
        mPlayback = playback;
        switch (oldState) {
            case PlaybackStateCompat.STATE_BUFFERING:
            case PlaybackStateCompat.STATE_CONNECTING:
            case PlaybackStateCompat.STATE_PAUSED:
                mPlayback.pause();
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentMusic();
                if (resumePlaying && currentMusic != null) {
                    mPlayback.play(currentMusic);
                } else if (!resumePlaying) {
                    mPlayback.pause();
                } else {
                    mPlayback.stop(true);
                }
                break;
            case PlaybackStateCompat.STATE_NONE:
                break;
            default:
        }
    }


    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            if (mQueueManager.getCurrentMusic() == null) {
                mQueueManager.setRandomQueue();
            }
            handlePlayRequest();
        }

        @Override
        public void onSkipToQueueItem(long queueId) {
            mQueueManager.setCurrentQueueItem(queueId);
            mQueueManager.updateMetadata();
        }

        @Override
        public void onSeekTo(long position) {
            mPlayback.seekTo((int) position);
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            mQueueManager.setQueueFromMusic(mediaId);
            handlePlayRequest();
            LogUtils.d("mCurrentPosition:::handlePlayRequest::onPlayFromMediaId");
        }

        @Override
        public void onPause() {
            handlePauseRequest();
        }

        @Override
        public void onStop() {
            handleStopRequest(null);
        }

        @Override
        public void onSkipToNext() {
            if (mQueueManager.skipQueuePosition(1)) {
                handlePlayRequest();
            } else {
                handleStopRequest("Cannot skip");
            }
            mQueueManager.updateMetadata();
        }

        @Override
        public void onSkipToPrevious() {
            if (mQueueManager.skipQueuePosition(-1)) {
                handlePlayRequest();
            } else {
                handleStopRequest("Cannot skip");
            }
            mQueueManager.updateMetadata();
        }

        @Override
        public void onCustomAction(@NonNull String action, Bundle extras) {
            if (action.equals(ORDER_ACTION)) {
                setOrderType(extras.getInt(ORDER_ACTION, ORDERLOOP));
            } else if (action.equals(MUSIC_ACTION)) {
                mServiceCallback.onCustomAction(action, extras);
            }
        }

        @Override
        public void onPlayFromSearch(final String query, final Bundle extras) {

            mPlayback.setState(PlaybackStateCompat.STATE_CONNECTING);
            boolean successSearch = mQueueManager.setQueueFromSearch(query, extras);
            if (successSearch) {
                handlePlayRequest();
                mQueueManager.updateMetadata();
            } else {
                updatePlaybackState("Could not find music");
            }
        }
    }

    public Playback getPlayback() {
        return mPlayback;
    }

    public MediaSessionCompat.Callback getMediaSessionCallback() {
        return mMediaSessionCallback;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        LogUtils.d("setOrderType:::" + orderType);
        if (orderType > 2 && orderType < 0) {
            return;
        }
        switch (orderType) { // 只需要 orderType了,在其他地方处理播放顺序
            case ORDERLOOP:
//                mQueueManager.setNormalQueue(currentMusicMediaId);
                break;
            case ORDERRANDOM:
//                mQueueManager.setRandomQueue(currentMusicMediaId);
                break;
            default:
                break;
        }
        this.orderType = orderType;
    }

    public interface PlaybackServiceCallback {
        void onPlaybackStart();

        void onNotificationRequired();

        void onPlaybackStop();

        void onPlaybackStateUpdated(PlaybackStateCompat newState);

        void onBufferingUpdate(int percent);

        void onCustomAction(String action, Bundle extras);
    }

}
