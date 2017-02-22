package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
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
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.LoopPagerRecyclerView;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.PagerRecyclerView;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.RecyclerViewUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
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

    private int mPointDuration = 500;
    private int mPointDegree = 25;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_music_paly;
    }

    @Override
    protected void initView(View rootView) {
        mStringList.add("");
        mStringList.add("");
        mStringList.add("");
        mStringList.add("");
        initListener();

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

        mFragmentMusicPalyRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        mFragmentMusicPalyRv.setFlingFactor(3f);
        mFragmentMusicPalyRv.setTriggerOffset(0.25f);
        mFragmentMusicPalyRv.setAdapter(mAdapter);
        mFragmentMusicPalyRv.setHasFixedSize(true);

        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
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

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
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
                break;
            case R.id.fragment_music_paly_lyrics:
                break;
            case R.id.fragment_music_paly_order:
                break;
            case R.id.fragment_music_paly_preview:// 上一首歌
                mFragmentMusicPalyRv.smoothScrollToPosition(mFragmentMusicPalyRv
                        .getActualCurrentPosition() - 1);
                doPointAnimation(0, mPointDuration);
                break;
            case R.id.fragment_music_paly_palyer:
//                mMusicPlayService.playOrPause();
                break;
            case R.id.fragment_music_paly_nextview:// 下一首歌
                mFragmentMusicPalyRv.smoothScrollToPosition(mFragmentMusicPalyRv
                        .getActualCurrentPosition() + 1);
                doPointAnimation(0, mPointDuration);
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

    private void initListener(){
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

}
