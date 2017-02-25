package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/02/20
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicPlayBackFragment extends TSFragment {

    @BindView(R.id.fragment_back_albun_art)
    ImageView mFragmentBackAlbunArt;
    @BindView(R.id.fragment_back_title)
    TextView mFragmentBackTitle;
    @BindView(R.id.fragment_back_artist)
    TextView mFragmentBackArtist;
    @BindView(R.id.fragment_back_extra_info)
    TextView mFragmentBackExtraInfo;
    @BindView(R.id.fragment_back_content)
    LinearLayout mFragmentBackContent;
    @BindView(R.id.fragment_back_play_pause)
    ImageButton mFragmentBackPlayPause;

    private String mArtUrl;

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_playback_controls;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity().getSupportMediaController() != null) {
            onConnected();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity().getSupportMediaController() != null) {
            getActivity().getSupportMediaController().unregisterCallback(mCallback);
        }
    }

    @OnClick({R.id.fragment_back_content, R.id.fragment_back_play_pause})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_back_content:
                Intent intent = new Intent(getActivity(), MusicPlayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                MediaControllerCompat controller = getActivity().getSupportMediaController();
                MediaMetadataCompat metadata = controller.getMetadata();
                if (metadata != null) {
                    intent.putExtra(MusicDetailActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION,
                            metadata.getDescription());
                }
                startActivity(intent);
                break;
            case R.id.fragment_back_play_pause:
                MediaControllerCompat controllerCompat = getActivity().getSupportMediaController();
                PlaybackStateCompat stateObj = controllerCompat.getPlaybackState();
                final int state = stateObj == null ?
                        PlaybackStateCompat.STATE_NONE : stateObj.getState();
                if (state == PlaybackStateCompat.STATE_PAUSED ||
                        state == PlaybackStateCompat.STATE_STOPPED ||
                        state == PlaybackStateCompat.STATE_NONE) {
                    playMedia();
                } else if (state == PlaybackStateCompat.STATE_PLAYING ||
                        state == PlaybackStateCompat.STATE_BUFFERING ||
                        state == PlaybackStateCompat.STATE_CONNECTING) {
                    pauseMedia();
                }
        }


    }

    public void onConnected() {
        MediaControllerCompat controller = getActivity().getSupportMediaController();
        if (controller != null) {
            onMetadataChanged(controller.getMetadata());
            onPlaybackStateChanged(controller.getPlaybackState());
            controller.registerCallback(mCallback);
        }
    }

    private void playMedia() {
        MediaControllerCompat controller = getActivity().getSupportMediaController();
        if (controller != null) {
            controller.getTransportControls().play();
        }
    }

    private void pauseMedia() {
        MediaControllerCompat controller = getActivity().getSupportMediaController();
        if (controller != null) {
            controller.getTransportControls().pause();
        }
    }

    private void onMetadataChanged(MediaMetadataCompat metadata) {
        if (getActivity() == null) {
            return;
        }
        if (metadata == null) {
            return;
        }

        mFragmentBackTitle.setText(metadata.getDescription().getTitle());
        mFragmentBackArtist.setText(metadata.getDescription().getSubtitle());
        String artUrl = null;
        if (metadata.getDescription().getIconUri() != null) {
            artUrl = metadata.getDescription().getIconUri().toString();
        }
        if (!TextUtils.equals(artUrl, mArtUrl)) {
            mArtUrl = artUrl;
            Bitmap art = metadata.getDescription().getIconBitmap();

        }
    }

    public void setExtraInfo(String extraInfo) {
        if (extraInfo == null) {
            mFragmentBackExtraInfo.setVisibility(View.GONE);
        } else {
            mFragmentBackExtraInfo.setText(extraInfo);
            mFragmentBackExtraInfo.setVisibility(View.VISIBLE);
        }
    }

    private void onPlaybackStateChanged(PlaybackStateCompat state) {
        if (getActivity() == null) {
            return;
        }
        if (state == null) {
            return;
        }
        boolean enablePlay = false;
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_STOPPED:
                enablePlay = true;
                break;
            case PlaybackStateCompat.STATE_ERROR:
                Toast.makeText(getActivity(), state.getErrorMessage(), Toast.LENGTH_LONG).show();
                break;
        }

        if (enablePlay) {
            mFragmentBackPlayPause.setImageDrawable(
                    ContextCompat.getDrawable(getActivity(), R.mipmap.music_ico_play));
        } else {
            mFragmentBackPlayPause.setImageDrawable(
                    ContextCompat.getDrawable(getActivity(), R.mipmap.music_ico_stop));
        }

        MediaControllerCompat controller = getActivity().getSupportMediaController();
        String extraInfo = null;
        if (controller != null && controller.getExtras() != null) {
            String castName = controller.getExtras().getString(MusicPlayService
                    .EXTRA_CONNECTED_CAST);
            if (castName != null) {
                extraInfo = "tym";
            }
        }
        setExtraInfo(extraInfo);
    }

    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            MusicPlayBackFragment.this.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata == null) {
                return;
            }
            MusicPlayBackFragment.this.onMetadataChanged(metadata);
        }
    };
}
