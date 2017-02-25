package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.ListPopupWindow;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.LoopPagerRecyclerView;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.PagerRecyclerView;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.RecyclerViewUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

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
    SeekBar mFragmentMusicPalyProgress;
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
    LinearLayout mFragmentMusicPalyBg;
    @BindView(R.id.fragment_music_paly_rv)
    LoopPagerRecyclerView mFragmentMusicPalyRv;
    @BindView(R.id.fragment_music_paly_cur_time)
    TextView mFragmentMusicPalyCurTime;
    @BindView(R.id.fragment_music_paly_total_time)
    TextView mFragmentMusicPalyTotalTime;

    private ImageLoader mImageLoader;

    private CommonAdapter mAdapter;

    private List<String> mStringList = new ArrayList<>();

    /**
     * 指针位置flag
     */
    private boolean isPointOutPhonograph;

    /**
     * 指针动画
     */
    private ObjectAnimator mPointAnimate;

    /**
     * 磁盘动画
     */
    private ObjectAnimator mPhonographAnimate;

    /**
     * 两个动画的暂停位置记录
     */
    private float[] points = new float[2];

    /**
     * 当前动画view
     */
    private ViewGroup mCurrentView;

    /**
     * 指针动画时长
     */
    private int mPointDuration = 500;

    /**
     * 指针角度
     */
    private int mPointDegree = 25;


    private MediaBrowserCompat mMediaBrowserCompat;

    private PlaybackStateCompat mLastPlaybackState;

    /**
     * 歌曲列表
     */
    private ListPopupWindow mListPopupWindow;

    /**
     * 音乐进度更新间隔
     */
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;

    /**
     * 音乐播放进度初始延迟
     */
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    /**
     * 进度条控制
     */
    private Subscription mProgressSubscription;
    private Observable<Long> mProgressObservable;

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
        if (savedInstanceState == null) {
            updateFromParams(getArguments());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMediaBrowserCompat != null) {
            mMediaBrowserCompat.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaBrowserCompat != null) {
            mMediaBrowserCompat.disconnect();
        }
        if (getActivity().getSupportMediaController() != null) {
            getActivity().getSupportMediaController().unregisterCallback(mCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rxStopProgress();
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
    protected void initView(View rootView) {
        initListener();

        mStringList.add("");
        mStringList.add("");
        mStringList.add("");
        mStringList.add("");

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

        dealBackgroud();
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
            .fragment_music_paly_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_music_paly_share:
                break;
            case R.id.fragment_music_paly_like:
                break;
            case R.id.fragment_music_paly_comment:
                mListPopupWindow.show();
                break;
            case R.id.fragment_music_paly_lyrics:
                break;
            case R.id.fragment_music_paly_order:
                break;
            case R.id.fragment_music_paly_preview:// 上一首歌
                mFragmentMusicPalyRv.smoothScrollToPosition(mFragmentMusicPalyRv
                        .getActualCurrentPosition() - 1);
                doPointAnimation(0, mPointDuration);
                getActivity().getSupportMediaController().getTransportControls().skipToPrevious();
                break;
            case R.id.fragment_music_paly_palyer:
                PlaybackStateCompat state = getActivity().getSupportMediaController()
                        .getPlaybackState();
                if (state != null) {
                    MediaControllerCompat.TransportControls controls =
                            getActivity().getSupportMediaController().getTransportControls();
                    switch (state.getState()) {
                        case PlaybackStateCompat.STATE_PLAYING: // fall through
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
            case R.id.fragment_music_paly_nextview:// 下一首歌
                mFragmentMusicPalyRv.smoothScrollToPosition(mFragmentMusicPalyRv
                        .getActualCurrentPosition() + 1);
                doPointAnimation(0, mPointDuration);
                getActivity().getSupportMediaController().getTransportControls().skipToNext();
                break;
            case R.id.fragment_music_paly_list:
                break;
            default:
                break;
        }
    }

    /**
     * 指针动画
     *
     * @param targetDegree 目标角度
     * @param duration     时长
     */
    public void doPointAnimation(int targetDegree, int duration) {
        mPointAnimate = ObjectAnimator.ofFloat(mFragmentMusicPalyPhonographPoint, "Rotation",
                points[0],
                targetDegree);
        // 设置持续时间
        mPointAnimate.setDuration(duration);
        mPointAnimate.setInterpolator(new AccelerateDecelerateInterpolator());
        // 设置动画监听
        mPointAnimate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                // 监听动画执行的位置，以便下次开始时，从当前位置开始
                points[0] = (float) animation.getAnimatedValue();
            }
        });

        mPointAnimate.start();
    }


    /**
     * 磁盘动画
     *
     * @param targetDegree 目标角度
     * @param duration     时长
     */
    public void doPhonographAnimation(int targetDegree, int duration) {
        View target;
        if (mCurrentView == null) {
            ViewGroup group = (ViewGroup) RecyclerViewUtils.getCenterXChild
                    (mFragmentMusicPalyRv);
            target = group.getChildAt(0);
        } else {
            target = mCurrentView.getChildAt(0);
        }

        mPhonographAnimate = ObjectAnimator.ofFloat(target, "Rotation",
                points[1],
                targetDegree);
        // 设置持续时间
        mPhonographAnimate.setDuration(duration);

        mPhonographAnimate.setInterpolator(new LinearInterpolator());
        mPhonographAnimate.setRepeatCount(ObjectAnimator.INFINITE);
        // 设置动画监听
        mPhonographAnimate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                // 监听动画执行的位置，以便下次开始时，从当前位置开始
                points[1] = (float) animation.getAnimatedValue();
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
            points[1] = 0;// 重置起始位置
        }
    }

    /**
     * 暂停转盘动画 *
     */
    public void pauseAnimation(View target) {
        if (target == null) target = RecyclerViewUtils.getCenterXChild(mFragmentMusicPalyRv);
        if (target != null && mPhonographAnimate != null) {
            mPhonographAnimate.cancel();
            target.clearAnimation();// 清除此ImageView身上的动画
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
                points[1] = 0f;
                stopAnimation(mCurrentView);

            }

            @Override
            public void OnDragging(int downPosition) {
                if (!isPointOutPhonograph) {
                    isPointOutPhonograph = true;
                    pauseAnimation(mCurrentView);
                    doPointAnimation(0, mPointDuration);
                }

            }

            @Override
            public void OnIdle(int position) {
                ToastUtils.showToast("OnIdle");
                mCurrentView = (ViewGroup) RecyclerViewUtils.getCenterXChild(mFragmentMusicPalyRv);
                if (mCurrentView != null) {
                    mCurrentView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doPhonographAnimation(360, 10000);
                        }
                    }, 500);
                }
                doPointAnimation(mPointDegree, mPointDuration);
                isPointOutPhonograph = false;
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
//            finish();
            ToastUtils.showToast("metadata_error");
            return;
        }
        getActivity().setSupportMediaController(mediaController);
        mediaController.registerCallback(mCallback);
        PlaybackStateCompat state = mediaController.getPlaybackState();

        updatePlaybackState(state);

        MediaMetadataCompat metadata = mediaController.getMetadata();
        if (metadata != null) {
            updateMediaDescription(metadata.getDescription());
            updateDuration(metadata);
        }
        updateProgress();
        if (state != null && (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                state.getState() == PlaybackStateCompat.STATE_BUFFERING)) {
            rxStartProgress();
        }
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
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                rxStopProgress();
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
        if (mLastPlaybackState == null) {
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

    private void dealBackgroud() {
        Bitmap bitmap = BitmapFactory
                .decodeResource(getResources(), R.mipmap.npc).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(grayColorFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawARGB(200, 255, 255, 255);

        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable.setFilterBitmap(true);
        mFragmentMusicPalyBg.setBackgroundDrawable(drawable);
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
                ToastUtils.showToast("position");
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
}
