package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayService;

public class MusicDetailActivity extends TSActivity<MusicDetailPresenter, MusicDetailFragment>
        implements MusicDetailFragment.MediaBrowserCompatProvider {

    private MusicDetailFragment mMusicDetailFragment;

    private MediaBrowserCompat mMediaBrowserCompat;

    private MusicPlayBackFragment mPlayBackFragment;

    public static final String EXTRA_START_FULLSCREEN =
            "com.zhiyicx.thinksnsplus.EXTRA_START_FULLSCREEN";

    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION =
            "com.zhiyicx.thinksnsplus.CURRENT_MEDIA_DESCRIPTION";

    private static final String SAVED_MEDIA_ID = "com.zhiyicx.thinksnsplus.MEDIA_ID";

    @Override
    protected MusicDetailFragment getFragment() {
        mMediaBrowserCompat = new MediaBrowserCompat(this, new ComponentName(this,
                MusicPlayService.class)
                , mConnectionCallback, null);
        mMusicDetailFragment = new MusicDetailFragment();
        return mMusicDetailFragment;
    }

    @Override
    protected void componentInject() {
        DaggerMusicDetailComponent.builder().appComponent(AppApplication.AppComponentHolder
                .getAppComponent()).musicDetailPresenterModule(new MusicDetailPresenterModule
                (mContanierFragment))
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayBackFragment = (MusicPlayBackFragment) getSupportFragmentManager().findFragmentById
                (R.id.fragment_playback_controls);
        if (mPlayBackFragment == null) {
            throw new IllegalStateException("Mising fragment with id 'controls'. Cannot continue.");
        }
        hidePlaybackControls();
        mMediaBrowserCompat.connect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String mediaId = getMediaId();
        if (mediaId != null) {
            outState.putString(SAVED_MEDIA_ID, mediaId);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getSupportMediaController() != null) {
            getSupportMediaController().unregisterCallback(mMediaControllerCallback);
        }
        mMediaBrowserCompat.disconnect();
    }

    @Override
    public MediaBrowserCompat getMediaBrowser() {
        return mMediaBrowserCompat;
    }

    public String getMediaId() {
        if (mMusicDetailFragment == null) {
            return null;
        }
        return mMusicDetailFragment.getMediaId();
    }

    protected void showPlaybackControls() {
        if (NetUtils.netIsConnected(this)) {
            getSupportFragmentManager().beginTransaction()
                    .show(mPlayBackFragment)
                    .commit();
        }
    }

    protected void hidePlaybackControls() {
        getSupportFragmentManager().beginTransaction()
                .hide(mPlayBackFragment)
                .commit();
    }

    protected boolean shouldShowControls() {
        if (getSupportMediaController() == null ||
                getSupportMediaController().getMetadata() == null ||
                getSupportMediaController().getPlaybackState() == null) {
            return false;
        }
        switch (getSupportMediaController().getPlaybackState().getState()) {
            case PlaybackStateCompat.STATE_ERROR:
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                return false;
            default:
                return true;
        }
    }

    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController = new MediaControllerCompat(this, token);
        setSupportMediaController(mediaController);
        mediaController.registerCallback(mMediaControllerCallback);

        if (shouldShowControls()) {
            showPlaybackControls();
        } else {
            hidePlaybackControls();
        }
        if (mPlayBackFragment != null) {
            mPlayBackFragment.onConnected();
        }
        onMediaControllerConnected();
    }

    protected void onMediaControllerConnected() {
        // empty implementation, can be overridden by clients.
    }

    public final MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        hidePlaybackControls();
                    }
                }

                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        hidePlaybackControls();
                    }
                }
            };

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    try {
                        connectToSession(mMediaBrowserCompat.getSessionToken());
                        mMusicDetailFragment.onConnected();
                    } catch (RemoteException e) {
                        hidePlaybackControls();
                    }
                }
            };


}
