package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideMusicBgTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.media_data.MusicProviderSource;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentHeader;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_helper.MediaIDHelper;
import com.zhiyicx.thinksnsplus.widget.MusicListPopupWindow;
import com.zhiyicx.thinksnsplus.widget.PlayerSeekBar;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.LoopPagerRecyclerView;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.PagerRecyclerView;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.RecyclerViewUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_ABLUM_COLLECT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_MUSIC_LIKE;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_MUSIC_CACHE_PROGRESS;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_MUSIC_COMPLETE;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_MUSIC_LOAD;
import static com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.PlaybackManager.ORDERLOOP;
import static com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.PlaybackManager.ORDERSINGLE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.PlaybackManager.ORDER_ACTION;
import static com.zhiyicx.thinksnsplus.modules.music_fm.media_data.MusicAblumInfo.METADATA_KEY_GENRE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailFragment.MUSIC_INFO;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 音乐播放主要界面
 */
public class MusicPlayFragment extends TSFragment<MusicPlayContract.Presenter> implements
        MusicPlayContract.View {

    @BindView(R.id.fragment_music_paly_phonograph_point)
    ImageView mFragmentMusicPalyPhonographPoint;
    @BindView(R.id.fragment_music_paly_share)
    ImageView mFragmentMusicPalyShare;
    @BindView(R.id.fragment_music_paly_like)
    ImageView mFragmentMusicPalyLike;
    @BindView(R.id.fragment_music_paly_comment)
    ImageView mFragmentMusicPalyComment;
    @BindView(R.id.fragment_music_paly_comment_count)
    TextView mFragmentMusicPalyCommentCount;
    @BindView(R.id.fragment_music_paly_lyrics)
    ImageView mFragmentMusicPalyLyrics;
    @BindView(R.id.fragment_music_paly_progress)
    PlayerSeekBar mFragmentMusicPalyProgress;
    @BindView(R.id.fragment_music_paly_order)
    ImageView mFragmentMusicPalyOrder;
    @BindView(R.id.fragment_music_paly_preview)
    ImageView mFragmentMusicPalyPreview;
    @BindView(R.id.fragment_music_paly_palyer)
    ImageView mFragmentMusicPalyPalyer;
    @BindView(R.id.fragment_music_paly_nextview)
    ImageView mFragmentMusicPalyNextview;
    @BindView(R.id.fragment_music_paly_list)
    ImageView mFragmentMusicPalyList;
    @BindView(R.id.fragment_music_paly_bg)
    ImageView mFragmentMusicPalyBg;
    @BindView(R.id.fragment_music_paly_rv)
    LoopPagerRecyclerView mFragmentMusicPalyRv;
    @BindView(R.id.fragment_music_paly_cur_time)
    TextView mFragmentMusicPalyCurTime;
    @BindView(R.id.fragment_music_paly_total_time)
    TextView mFragmentMusicPalyTotalTime;
    @BindView(R.id.fragment_music_paly_lrc)
    TextView mFragmentMusicPalyLrc;


    /**
     * 播放进度条更新间隔
     */
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    /**
     * 播放进度条初始延迟
     */
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    private ImageLoader mImageLoader;
    private CommonAdapter mAdapter;
    private List<MusicAlbumDetailsBean.MusicsBean> mMusicList = new ArrayList<>();
    /**
     * 播放模式
     */
    private int mDefalultOrder;
    /**
     * 播放模式图标
     */
    private Integer[] mOrderModule = new Integer[]{R.mipmap.music_ico_random, R.mipmap
            .music_ico_single, R.mipmap.music_ico_inorder};

    private boolean isConnected;

    private boolean isComplete;

    private MediaDescriptionCompat mMediaDescriptionCompat;
    private MusicAlbumDetailsBean mMusicAlbumDetailsBean;
    private MusicAlbumDetailsBean.MusicsBean mCurrentMusic;
    private String mCurrentMediaId = "-1";

    /**
     * 指针位置flag
     */
    private boolean isDraging;

    /**
     * 指针动画
     */
    private ObjectAnimator mPointAnimate;

    /**
     * 磁盘动画
     */
    private ObjectAnimator mPhonographAnimate;

    /**
     * 磁盘动画的暂停位置记录
     */
    private float mCurrentValue;

    /**
     * 当前动画view
     */
    private ViewGroup mCurrentView;

    private MediaBrowserCompat mMediaBrowserCompat;

    private PlaybackStateCompat mLastPlaybackState;

    /**
     * 歌曲列表
     */
    private MusicListPopupWindow mListPopupWindow;

    /**
     * 进度条控制
     */
    private Subscription mProgressSubscription;
    private Observable<Long> mProgressObservable;

    /**
     * 音乐播放切换事件回调
     */
    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata != null) {
                //noinspection ResourceType
                String musicUrl = metadata.getString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE);
                boolean isCached = AppApplication.getProxy().isCached(musicUrl);
                LogUtils.d("isCached:::" + isCached);
                LogUtils.d("thread:::" + Thread.currentThread().getName());
                if (isCached) {
                    mFragmentMusicPalyProgress.setSecondaryProgress(70);
                }
                LogUtils.d("Second:::" + mFragmentMusicPalyProgress.getSecondaryProgress());
                mToolbarCenter.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_ALBUM));
                mCurrentMediaId = metadata.getDescription().getMediaId();
                mListPopupWindow.getAdapter().notifyDataSetChanged();
                updateMediaDescription(metadata.getDescription());
                updateDuration(metadata);
            }
        }
    };

    /**
     * 音乐播放服务连接回调
     */
    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    try {
                        connectToSession(mMediaBrowserCompat.getSessionToken());
                    } catch (RemoteException e) {

                    }
                }
            };

    public static MusicPlayFragment newInstance(Bundle args) {
        MusicPlayFragment fragment = new MusicPlayFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaBrowserCompat = new MediaBrowserCompat(getActivity(), new ComponentName(getActivity(),
                MusicPlayService.class)
                , mConnectionCallback, null);
        if (mMediaBrowserCompat.isConnected()) {
            mMediaBrowserCompat.disconnect();
        }
        if (savedInstanceState == null) {
            updateFromParams(getArguments());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMediaBrowserCompat != null) {
            mDefalultOrder = SharePreferenceUtils.getInterger(getActivity(),
                    SharePreferenceTagConfig.SHAREPREFERENCE_TAG_MUSIC);
            if (mDefalultOrder != -1) {
                mListPopupWindow.setOrder(mDefalultOrder);
                mFragmentMusicPalyOrder.setImageResource(mOrderModule[mDefalultOrder]);
            } else {
                mDefalultOrder = ORDERLOOP;
            }
            if (!mMediaBrowserCompat.isConnected()) {
                mMediaBrowserCompat.connect();
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaBrowserCompat != null) {// 状态重置
            isConnected = false;
            mMediaBrowserCompat.disconnect();// 结束断开，启动后重新连接
        }
        if (getActivity().getSupportMediaController() != null) {
            getActivity().getSupportMediaController().unregisterCallback(mCallback);
        }
        rxStopProgress();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_music_paly;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return "氷は雪はなく";
    }

    @Override
    protected void initView(View rootView) {
        mFragmentMusicPalyProgress.setThumb(R.mipmap.music_pic_progressbar_circle);
        initListener();
        mToolbarCenter.setText(AppApplication.getmQueueManager().getCurrentMusic().getDescription().getTitle());
        if (getArguments() != null) {

        }
        mMusicAlbumDetailsBean = (MusicAlbumDetailsBean) getArguments().getSerializable
                (MUSIC_INFO);
        mMusicList = mMusicAlbumDetailsBean.getMusics();

        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        MediaSessionCompat.QueueItem mCurrentMusic = AppApplication.getmQueueManager().getCurrentMusic();
        if (mCurrentMusic != null) {
            mCurrentMediaId = MediaIDHelper.extractMusicIDFromMediaID(mCurrentMusic.getDescription().getMediaId());
        }
        mFragmentMusicPalyLrc.setMovementMethod(ScrollingMovementMethod.getInstance());
        mListPopupWindow = MusicListPopupWindow.Builder()
                .with(getActivity())
                .alpha(0.8f)
                .data(mMusicList)
                .adapter(getPopListAdapter())
                .build();

        mFragmentMusicPalyRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        mFragmentMusicPalyRv.setFlingFactor(3f);
        mFragmentMusicPalyRv.setTriggerOffset(0.25f);
        mFragmentMusicPalyRv.setAdapter(getMediaListAdapter());
        mFragmentMusicPalyRv.setHasFixedSize(true);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(MusicPlayContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    /**
     * 各类监听器
     */
    private void initListener() {

        mFragmentMusicPalyProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener
                () {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFragmentMusicPalyCurTime.setText(DateUtils.formatElapsedTime(progress / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                rxStopProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getActivity().getSupportMediaController().getTransportControls().seekTo(seekBar
                        .getProgress());
                rxStartProgress();
            }
        });

        mFragmentMusicPalyPhonographPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int w = mFragmentMusicPalyPhonographPoint.getWidth();
                int h = mFragmentMusicPalyPhonographPoint.getHeight();
                mFragmentMusicPalyPhonographPoint.setPivotX(9 * w / 10);
                mFragmentMusicPalyPhonographPoint.setPivotY(h / 2);
            }
        });

        mFragmentMusicPalyRv.addOnPageChangedListener(new PagerRecyclerView.OnPageChangedListener
                () {

            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                mCurrentValue = 0;
                stopAnimation(mCurrentView);

                if (isConnected && !isComplete) {
                    if (newPosition > oldPosition) {
                        getActivity().getSupportMediaController().getTransportControls()
                                .skipToNext();
                    } else {
                        getActivity().getSupportMediaController().getTransportControls()
                                .skipToPrevious();
                    }
                }
            }

            @Override
            public void OnDragging(int downPosition) {
                if (!isDraging) {
                    pauseAnimation();
                }
                isDraging = true;
            }

            @Override
            public void OnIdle(int position) {
//                ToastUtils.showToast("OnIdle");
                mCurrentView = (ViewGroup) RecyclerViewUtils.getCenterXChild(mFragmentMusicPalyRv);
                isDraging = false;
                if (mCurrentValue != 0 || isComplete) {
                    doPhonographAnimation();
                }
                isComplete = false;
            }

        });
    }

    /**
     * 连接上音乐播放服务
     *
     * @param token
     * @throws RemoteException
     */
    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController = new MediaControllerCompat(
                getActivity(), token);
        if (mediaController.getMetadata() == null) {
            ToastUtils.showToast("metadata_error");
            getActivity().finish();
            return;
        }
        mediaController.getTransportControls()
                .sendCustomAction(mDefalultOrder + "", null);
        getActivity().setSupportMediaController(mediaController);
        mediaController.registerCallback(mCallback);
        PlaybackStateCompat state = mediaController.getPlaybackState();

        updatePlaybackState(state);

        MediaMetadataCompat metadata = mediaController.getMetadata();

        if (metadata != null) {
            updateMediaDescription(metadata.getDescription());
            updateDuration(metadata);
        }
        if (state != null && (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                state.getState() == PlaybackStateCompat.STATE_BUFFERING)) {
//            rxStartProgress();
        }
        isConnected = true;
    }

    /**
     * 更新音乐信息
     *
     * @param description 音乐信息数据
     */
    private void updateMediaDescription(final MediaDescriptionCompat description) {
        if (description == null) {
            return;
        } else {
            mMediaDescriptionCompat = description;
            mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                    .transformation(new GlideMusicBgTransform(getActivity()))
                    .errorPic(R.drawable.shape_default_image)
                    .placeholder(R.drawable.shape_default_image)
                    .imagerView(mFragmentMusicPalyBg)
                    .url(description.getIconUri() + "")
                    .build());
            View item = RecyclerViewUtils
                    .getCenterXChild(mFragmentMusicPalyRv);

            ImageView image = (ImageView) item.findViewById(R.id.fragment_music_paly_img);

            mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                    .imagerView(image)
                    .errorPic(R.drawable.shape_default_image)
                    .placeholder(R.drawable.shape_default_image)
                    .url(description.getIconUri() + "")
                    .build());

            Observable.from(mMusicList).filter(new Func1<MusicAlbumDetailsBean.MusicsBean, Boolean>() {
                @Override
                public Boolean call(MusicAlbumDetailsBean.MusicsBean musicsBean) {
                    return (musicsBean.getId() + "").equals(description.getMediaId());
                }
            }).subscribe(new Action1<MusicAlbumDetailsBean.MusicsBean>() {
                @Override
                public void call(MusicAlbumDetailsBean.MusicsBean musicsBean) {
                    mCurrentMusic = musicsBean;
                }
            });

            if (mCurrentMusic.getMusic_info().getComment_count() > 0) {
                mFragmentMusicPalyComment.setImageResource(
                        R.mipmap.music_ico_comment_incomplete);
                mFragmentMusicPalyCommentCount.setText("" + mCurrentMusic.getMusic_info().getComment_count());
            } else {
                mFragmentMusicPalyComment.setImageResource(
                        R.mipmap.music_ico_comment_complete);
                mFragmentMusicPalyCommentCount.setText("");
            }


            mFragmentMusicPalyLrc.setText(mCurrentMusic.getMusic_info().getLyric());

            if (mCurrentMusic.getMusic_info().getIsdiggmusic() == 1) {
                mFragmentMusicPalyLike.setImageResource(R.mipmap.music_ico_like_high);
            } else {
                mFragmentMusicPalyLike.setImageResource(R.mipmap.music_ico_like_normal);
            }
        }

    }

    private void updateDuration(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        int duration = (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        mFragmentMusicPalyProgress.setMax(duration);
        mFragmentMusicPalyTotalTime.setText(DateUtils.formatElapsedTime(duration / 1000));
    }

    private void updatePlaybackState(PlaybackStateCompat state) {
        if (state == null) {
            return;
        }
        mLastPlaybackState = state;
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                rxStartProgress();
                if (mPhonographAnimate == null || !mPhonographAnimate.isStarted()) {
                    doPhonographAnimation();
                }
                mFragmentMusicPalyPalyer.setImageResource(R.mipmap.music_ico_stop);
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                rxStopProgress();
                if (mPhonographAnimate != null && mPhonographAnimate.isStarted()) {
                    pauseAnimation();
                }
                mFragmentMusicPalyPalyer.setImageResource(R.mipmap.music_ico_play);
                break;
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                rxStopProgress();
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                rxStopProgress();
                break;
            default:
        }

    }

    /**
     * 更新音乐进度
     */
    private void rxStartProgress() {
        if (mProgressObservable == null) {
            mProgressObservable = Observable.interval(PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit
                            .MILLISECONDS);
        }
        mProgressSubscription = mProgressObservable
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        updateProgress();
                    }
                });
    }

    private void rxStopProgress() {
        if (mProgressSubscription != null) {
            mProgressSubscription.unsubscribe();
        }
    }

    private void updateProgress() {
        if (mLastPlaybackState == null || mFragmentMusicPalyProgress == null) {
            return;
        }
        long currentPosition = mLastPlaybackState.getPosition();
        if (mLastPlaybackState.getState() != PlaybackStateCompat.STATE_PAUSED) {
            long timeDelta = SystemClock.elapsedRealtime() -
                    mLastPlaybackState.getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * mLastPlaybackState.getPlaybackSpeed();
        }
        mFragmentMusicPalyProgress.setProgress((int) currentPosition);
    }

    /**
     * 初始信息
     *
     * @param bundle
     */
    private void updateFromParams(Bundle bundle) {
        if (bundle != null) {
            MediaDescriptionCompat description = bundle.getParcelable(MusicDetailActivity
                    .EXTRA_CURRENT_MEDIA_DESCRIPTION);
            if (description != null) {
                updateMediaDescription(description);
            }
        }
    }

    @Subscriber(tag = EVENT_SEND_MUSIC_CACHE_PROGRESS, mode = ThreadMode.MAIN)
    public void onBufferingUpdate(int progress) {
        mFragmentMusicPalyProgress.setSecondaryProgress(progress);
        LogUtils.d("MUSIC_CACHE_PROGRESS", "" + progress);
    }

    @Subscriber(tag = EVENT_SEND_MUSIC_LOAD, mode = ThreadMode.MAIN)
    public void onMusicLoading(boolean l) {
        LogUtils.d("MUSIC_LOADING", "" + l + "");
        mFragmentMusicPalyProgress.setLoading(l);
    }

    @Subscriber(tag = EVENT_SEND_MUSIC_COMPLETE, mode = ThreadMode.MAIN)
    public void onMusicEnd(int orderType) {
        isComplete = true;
        if (orderType != ORDERSINGLE) {
            doPointOutAnimation(500, 0);
            mFragmentMusicPalyRv.setSpeed(100);
            mFragmentMusicPalyRv.smoothScrollToPosition(mFragmentMusicPalyRv
                    .getCurrentPosition() + 1);
            mFragmentMusicPalyRv.setSpeed(250);
        }
        LogUtils.d("MUSIC_END", "" + orderType);
    }

    /**
     * 指针动画
     */
    public void doPointInAnimation(int duration, long delay) {
        mPointAnimate = ObjectAnimator.ofFloat(mFragmentMusicPalyPhonographPoint, "Rotation",
                0,
                25);
        // 设置持续时间
        mPointAnimate.setDuration(duration);
        mPointAnimate.setStartDelay(delay);
        mPointAnimate.setInterpolator(new AccelerateDecelerateInterpolator());
        mPointAnimate.start();
    }

    public void doPointOutAnimation(int duration, long delay) {
        mPointAnimate = ObjectAnimator.ofFloat(mFragmentMusicPalyPhonographPoint, "Rotation",
                25,
                0);
        // 设置持续时间
        mPointAnimate.setDuration(duration);
        mPointAnimate.setStartDelay(delay);
        mPointAnimate.setInterpolator(new AccelerateDecelerateInterpolator());
        mPointAnimate.start();
    }


    /**
     * 磁盘动画
     */
    public void doPhonographAnimation() {
        doPointInAnimation(500, 0);
        View target;
        if (mCurrentView == null && mFragmentMusicPalyRv != null) {
            mCurrentView = (ViewGroup) RecyclerViewUtils.getCenterXChild
                    (mFragmentMusicPalyRv);
        }
        target = mCurrentView.getChildAt(0);

        mPhonographAnimate = ObjectAnimator.ofFloat(target, "Rotation",
                mCurrentValue,
                mCurrentValue + 360);
        // 设置持续时间
        mPhonographAnimate.setDuration(10000);

        mPhonographAnimate.setInterpolator(new LinearInterpolator());
        mPhonographAnimate.setRepeatCount(ObjectAnimator.INFINITE);
        // 设置动画监听
        mPhonographAnimate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                // 监听动画执行的位置，以便下次开始时，从当前位置开始
                mCurrentValue = (float) animation.getAnimatedValue();
            }
        });
        mPhonographAnimate.start();
    }

    /**
     * 停止转盘动画
     */
    public void stopAnimation(View target) {
        if (target != null && mPhonographAnimate != null) {
            mPhonographAnimate.setDuration(0);
            mPhonographAnimate.reverse();
            mPhonographAnimate.end();
            target.clearAnimation();
            mCurrentValue = 0;// 重置起始位置
        }
    }

    /**
     * 暂停转盘动画 *
     */
    public void pauseAnimation() {
        if (mCurrentView == null) {
            mCurrentView = (ViewGroup) RecyclerViewUtils.getCenterXChild
                    (mFragmentMusicPalyRv);
        }
        if (mCurrentView != null && mPhonographAnimate != null) {
            mPhonographAnimate.cancel();
            mCurrentView.getChildAt(0).clearAnimation();// 清除此ImageView身上的动画
        }
        doPointOutAnimation(500, 100);
    }

    @NonNull
    private CommonAdapter getPopListAdapter() {
        CommonAdapter adapter = new CommonAdapter<MusicAlbumDetailsBean.MusicsBean>(getActivity()
                , R.layout.item_music_detail_list, mMusicList) {
            @Override
            protected void convert(ViewHolder holder, MusicAlbumDetailsBean.MusicsBean s, int
                    position) {
                TextView musicName = holder.getView(R.id.item_music_name);
                TextView authorName = holder.getView(R.id.item_music_author);
                musicName.setText(s.getMusic_info().getTitle());
                authorName.setText("-" + s.getMusic_info().getSinger().getName());

                if (mCurrentMediaId.equals(s.getMusic_info().getId() + "")) {
                    musicName.setTextColor(getResources().getColor(R.color.important_for_theme));
                    authorName.setTextColor(getResources().getColor(R.color.important_for_theme));
                } else {
                    musicName.setTextColor(getResources().getColor(R.color.important_for_content));
                    authorName.setTextColor(getResources().getColor(R.color
                            .normal_for_assist_text));
                }

            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MusicAlbumDetailsBean.MusicsBean musicsBean = mMusicList.get(position);
                MediaControllerCompat controllerCompat = getActivity()
                        .getSupportMediaController();
                String id = MediaIDHelper.createMediaID("" + musicsBean.getMusic_info().getId(), MEDIA_ID_MUSICS_BY_GENRE, METADATA_KEY_GENRE);
                controllerCompat.getTransportControls()
                        .playFromMediaId(id, null);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
        return adapter;
    }

    @NonNull
    private CommonAdapter getMediaListAdapter() {
        mAdapter = new CommonAdapter<MusicAlbumDetailsBean.MusicsBean>(getActivity(),
                R.layout.item_music_play, mMusicList) {
            @Override
            protected void convert(ViewHolder holder, MusicAlbumDetailsBean.MusicsBean o, final
            int position) {
                ImageView image = holder.getView(R.id.fragment_music_paly_img);
                String imageUrl = String.format(ApiConfig.NO_PROCESS_IMAGE_PATH,
                        o.getMusic_info().getSinger().getCover().getId(), 50);
                mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                        .imagerView(image)
                        .errorPic(R.drawable.shape_default_image)
                        .placeholder(R.drawable.shape_default_image)
                        .url(imageUrl)
                        .build());
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return true;
            }
        });
        return mAdapter;
    }

    private void handleLrc() {
        mFragmentMusicPalyRv.setVisibility(mFragmentMusicPalyLrc.getVisibility() == View
                .VISIBLE ? View.VISIBLE : View.GONE);

        mFragmentMusicPalyPhonographPoint.setVisibility(mFragmentMusicPalyLrc.getVisibility() ==
                View
                        .VISIBLE ? View.VISIBLE : View.GONE);

        mFragmentMusicPalyLrc.setVisibility(mFragmentMusicPalyLrc.getVisibility() == View
                .VISIBLE ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.fragment_music_paly_share, R.id.fragment_music_paly_like, R.id
            .fragment_music_paly_comment, R.id.fragment_music_paly_lyrics, R.id
            .fragment_music_paly_order, R.id.fragment_music_paly_preview, R.id
            .fragment_music_paly_palyer, R.id.fragment_music_paly_nextview, R.id
            .fragment_music_paly_list, R.id.fragment_music_paly_bg, R.id.fragment_music_paly_lrc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_music_paly_share:// 分享
                mPresenter.shareMusic();
                break;
            case R.id.fragment_music_paly_like: // 点赞
                mPresenter.handleLike(mCurrentMusic.getMusic_info().getIsdiggmusic() == 0,
                        mMediaDescriptionCompat.getMediaId());
                if (mCurrentMusic.getMusic_info().getIsdiggmusic() == 1) {
                    mCurrentMusic.getMusic_info().setIsdiggmusic(0);
                    mFragmentMusicPalyLike.setImageResource(R.mipmap.music_ico_like_normal);
                } else {
                    mFragmentMusicPalyLike.setImageResource(R.mipmap.music_ico_like_high);
                    mCurrentMusic.getMusic_info().setIsdiggmusic(1);
                }
                EventBus.getDefault().post(mCurrentMusic,EVENT_MUSIC_LIKE);
                break;
            case R.id.fragment_music_paly_comment: // 评论
                Intent intent = new Intent(getActivity(), MusicCommentActivity.class);
                Bundle musicBundle = new Bundle();
                MusicCommentHeader.HeaderInfo headerInfo = new MusicCommentHeader.HeaderInfo();
                headerInfo.setCommentCount(mCurrentMusic.getMusic_info().getComment_count());
                headerInfo.setId(mCurrentMusic.getId());
                headerInfo.setTitle(mCurrentMusic.getMusic_info().getTitle());
                headerInfo.setLitenerCount(mCurrentMusic.getMusic_info().getTaste_count() + "");
                headerInfo.setImageUrl(ImageUtils.imagePathConvert(mCurrentMusic.getMusic_info().getSinger().getCover().getId() + "",
                        ImageZipConfig.IMAGE_70_ZIP));
                musicBundle.putSerializable(CURRENT_COMMENT, headerInfo);
                musicBundle.putString(CURRENT_COMMENT_TYPE, CURRENT_COMMENT_TYPE_MUSIC);
                intent.putExtra(CURRENT_COMMENT, musicBundle);
                startActivity(intent);
                break;
            case R.id.fragment_music_paly_order: // 播放顺序
                mDefalultOrder++;
                if (mDefalultOrder > 2) {
                    mDefalultOrder = 0;
                }
                mListPopupWindow.setOrder(mDefalultOrder);
                Bundle bundle = new Bundle();
                bundle.putInt(ORDER_ACTION, mDefalultOrder);
                getActivity().getSupportMediaController().getTransportControls()
                        .sendCustomAction(ORDER_ACTION, bundle);

                mFragmentMusicPalyOrder.setImageResource(mOrderModule[mDefalultOrder]);
                SharePreferenceUtils.setInterger(getActivity(),
                        SharePreferenceTagConfig.SHAREPREFERENCE_TAG_MUSIC, mDefalultOrder);

                break;
            case R.id.fragment_music_paly_preview:// 上一首歌
                rxStopProgress();
                if (mPhonographAnimate != null && mPhonographAnimate.isStarted()) {
                    pauseAnimation();
                }
                mFragmentMusicPalyRv.smoothScrollToPosition(
                        mFragmentMusicPalyRv.getCurrentPosition() - 1
                );

                mFragmentMusicPalyProgress.setProgress(0);
                break;
            case R.id.fragment_music_paly_palyer:// 播放暂停
                PlaybackStateCompat state = getActivity().getSupportMediaController()
                        .getPlaybackState();
                if (state != null) {
                    MediaControllerCompat.TransportControls controls =
                            getActivity().getSupportMediaController().getTransportControls();
                    switch (state.getState()) {
                        case PlaybackStateCompat.STATE_PLAYING:
                        case PlaybackStateCompat.STATE_BUFFERING:
                            controls.pause();
                            rxStopProgress();
                            break;
                        case PlaybackStateCompat.STATE_PAUSED:
                        case PlaybackStateCompat.STATE_STOPPED:
                            controls.play();
                            rxStartProgress();
                            break;
                        default:
                            break;
                    }
                }
                break;
            case R.id.fragment_music_paly_nextview:// 下一首
                rxStopProgress();
                if (mPhonographAnimate != null && mPhonographAnimate.isStarted()) {
                    pauseAnimation();
                }

                mFragmentMusicPalyRv.smoothScrollToPosition(
                        mFragmentMusicPalyRv.getCurrentPosition() + 1
                );
                mFragmentMusicPalyProgress.setProgress(0);
                break;
            case R.id.fragment_music_paly_list:// 歌曲目录
                mListPopupWindow.show();
                break;
//            case R.id.fragment_music_paly_bg:
            case R.id.fragment_music_paly_lyrics:// 歌词显示
//            case R.id.fragment_music_paly_lrc:
                handleLrc();
                break;
            default:
                break;
        }
    }
}
