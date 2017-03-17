package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.content.ComponentName;
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
import android.view.LayoutInflater;
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
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideMusicBgTransform;
import com.zhiyicx.baseproject.widget.popwindow.ListPopupWindow;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhiyicx.thinksnsplus.widget.PlayerSeekBar;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.LoopPagerRecyclerView;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.PagerRecyclerView;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.RecyclerViewUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_MUSIC_CACHE_PROGRESS;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_MUSIC_COMPLETE;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_MUSIC_LOAD;
import static com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.PlaybackManager.ORDERLOOP;
import static com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.PlaybackManager.ORDERSINGLE;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 音乐播放界面
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

    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;


    private ImageLoader mImageLoader;

    private CommonAdapter mAdapter;

    private List<String> mStringList = new ArrayList<>();

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
    private ListPopupWindow mListPopupWindow;

    /**
     * 进度条控制
     */
    private Subscription mProgressSubscription;
    private Observable<Long> mProgressObservable;
    private int mDefalultOrder;
    private Integer[] mOrderModule = new Integer[]{R.mipmap.music_ico_random, R.mipmap
            .music_ico_single, R.mipmap.music_ico_inorder};
    private boolean isConnected;
    private boolean isComplete;
    private boolean isSeekTo;

    /**
     * 音乐播放事件回调
     */
    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata != null) {
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
//        if (mMediaBrowserCompat != null) {
//            isConnected = false;
//            mMediaBrowserCompat.disconnect();
//        }
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
        initListener();

        mStringList.add("");
        mStringList.add("");
        mStringList.add("");
        mStringList.add("");
        mFragmentMusicPalyProgress.setIndeterminate(false);
        mFragmentMusicPalyLrc.setMovementMethod(ScrollingMovementMethod.getInstance());
        mListPopupWindow = ListPopupWindow.Builder()
                .with(getActivity())
                .alpha(0.8f)
                .data(mStringList)
                .adapter(getPopListAdapter())
                .build();

        mFragmentMusicPalyRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        mFragmentMusicPalyRv.setFlingFactor(3f);
        mFragmentMusicPalyRv.setTriggerOffset(0.25f);
        mFragmentMusicPalyRv.setAdapter(getMediaListAdapter());
        mFragmentMusicPalyRv.setHasFixedSize(true);

        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                .transformation(new GlideMusicBgTransform(getActivity()))
                .imagerView(mFragmentMusicPalyBg)
                .resourceId(R.mipmap.npc)
                .build());
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

    @OnClick({R.id.fragment_music_paly_share, R.id.fragment_music_paly_like, R.id
            .fragment_music_paly_comment, R.id.fragment_music_paly_lyrics, R.id
            .fragment_music_paly_order, R.id.fragment_music_paly_preview, R.id
            .fragment_music_paly_palyer, R.id.fragment_music_paly_nextview, R.id
            .fragment_music_paly_list, R.id.fragment_music_paly_bg, R.id.fragment_music_paly_lrc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_music_paly_share:
                break;
            case R.id.fragment_music_paly_like:
                break;
            case R.id.fragment_music_paly_comment:
                mStringList.add("");
                mStringList.add("");
                mListPopupWindow.dataChange(mStringList);
                break;
            case R.id.fragment_music_paly_order:
                getActivity().getSupportMediaController().getTransportControls()
                        .sendCustomAction(mDefalultOrder + "",null);
                mFragmentMusicPalyOrder.setImageResource(mOrderModule[mDefalultOrder]);
                SharePreferenceUtils.setInterger(getActivity(),
                        SharePreferenceTagConfig.SHAREPREFERENCE_TAG_MUSIC, mDefalultOrder);
                mDefalultOrder++;
                if (mDefalultOrder > 2) {
                    mDefalultOrder = 0;
                }

                break;
            case R.id.fragment_music_paly_preview:// 上一首歌
                rxStopProgress();
                if (mPhonographAnimate != null && mPhonographAnimate.isStarted()) {
                    pauseAnimation();
                }
                mFragmentMusicPalyRv.smoothScrollToPosition(mFragmentMusicPalyRv
                        .getActualCurrentPosition() - 1);
                mFragmentMusicPalyProgress.setProgress(0);
                break;
            case R.id.fragment_music_paly_palyer:
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
                mFragmentMusicPalyRv.smoothScrollToPosition(mFragmentMusicPalyRv
                        .getActualCurrentPosition() + 1);
                mFragmentMusicPalyProgress.setProgress(0);
                break;
            case R.id.fragment_music_paly_list:
                mListPopupWindow.show();
                break;
            case R.id.fragment_music_paly_bg:
            case R.id.fragment_music_paly_lyrics:
//            case R.id.fragment_music_paly_lrc:
                if (mFragmentMusicPalyLrc.getVisibility() == View.VISIBLE) {
                    hideLrc();
                } else {
                    showLrc();
                }
                break;
            default:
                break;
        }
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
                ToastUtils.showToast("OnIdle");
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

    private void updateMediaDescription(MediaDescriptionCompat description) {
        if (description == null) {

            return;
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
        mFragmentMusicPalyProgress.setSecondaryProgress(mFragmentMusicPalyProgress.getMax()
                * progress);
        Log.e("MUSIC_CACHE_PROGRESS", "" + progress);
    }

    @Subscriber(tag = EVENT_SEND_MUSIC_LOAD, mode = ThreadMode.MAIN)
    public void onMusicLoading(boolean l) {
        Log.e("MUSIC_LOAD", "" + l + "");
        mFragmentMusicPalyProgress.setLoading(l);
    }

    @Subscriber(tag = EVENT_SEND_MUSIC_COMPLETE, mode = ThreadMode.MAIN)
    public void onMusicEnd(int orderType) {
        isComplete = true;
        if (orderType != ORDERSINGLE) {
            doPointOutAnimation(500, 0);
            mFragmentMusicPalyRv.setSpeed(100);
            mFragmentMusicPalyRv.smoothScrollToPosition(mFragmentMusicPalyRv
                    .getActualCurrentPosition() + 1);
            mFragmentMusicPalyRv.setSpeed(250);
        }
        Log.e("MUSIC_END", "" + orderType);
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
        CommonAdapter adapter = new CommonAdapter<String>(getActivity(), R.layout
                .item_music_pop_list,
                mStringList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ToastUtils.showToast("position:" + position);
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
        mAdapter = new CommonAdapter<String>(getActivity(), R.layout.item_music_play, mStringList) {
            @Override
            protected void convert(ViewHolder holder, String o, final int position) {
                ImageView imageView = holder.getView(R.id.fragment_music_paly_phonograph);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ToastUtils.showToast("position");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
        return mAdapter;
    }

    private void hideLrc() {
        mFragmentMusicPalyLrc.setVisibility(View.GONE);
        mFragmentMusicPalyRv.setVisibility(View.VISIBLE);
        mFragmentMusicPalyPhonographPoint.setVisibility(View.VISIBLE);
//        mFragmentMusicPalyRv.scrollToPosition(mFragmentMusicPalyRv.getCurrentPosition());
    }

    private void showLrc() {
        mFragmentMusicPalyRv.setVisibility(View.GONE);
        mFragmentMusicPalyPhonographPoint.setVisibility(View.GONE);
        mFragmentMusicPalyLrc.setText("dfjie \n sdadw\n dsadw\n sawfsdw\n sdwdawdaw\n" +
                "dsfese\n wwoifej\n dieieuww\n eueueu\n iwoidbw\n uwiqwh\n sadiuwu\n " +
                "wuuroh\n" +
                "dfioeii\n cncjdj\n kjjsdjoieo\n euiwuvc\n oirklsdh\n ueudif\n dfjie \n" +
                " sdadw\n" +
                " dsadw\n" +
                " sawfsdw\n" +
                " sdwdawdaw\n" +
                "\" +\n" +
                "                        \"dsfese\\n wwoifej\\n dieieuww\\n eueueu\\n " +
                "iwoidbw\\n uwiqwh\\n sadiuwu\\n \" +\n" +
                "                        \"wuuroh\\n\" +\n" +
                "                        \"dfioeii\n" +
                " cncjdj\n" +
                " kjjsdjoieo\n");
        mFragmentMusicPalyLrc.setVisibility(View.VISIBLE);
    }

}
