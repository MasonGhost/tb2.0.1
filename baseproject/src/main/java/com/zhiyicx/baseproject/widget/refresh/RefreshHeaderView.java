package com.zhiyicx.baseproject.widget.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.log.LogUtils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */
public class RefreshHeaderView extends LinearLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private ImageView mPullDownView;
    private ImageView mReleaseRefreshingView;

    private AnimationDrawable mChangeToReleaseRefreshAd;
    private AnimationDrawable mRefreshingAd;

    private int mChangeToReleaseRefreshAnimResId;
    private int mRefreshingAnimResId;

    private int headerViewHeight;

    public RefreshHeaderView(Context context) {
        super(context);
        init(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRefreshingAnimResId = cn.bingoogolapple.refreshlayout.R.drawable.refresh_loading;
        mChangeToReleaseRefreshAnimResId = cn.bingoogolapple.refreshlayout.R.drawable.refresh_loading;

        LayoutInflater.from(context).inflate(R.layout.vw_header, this);
        mPullDownView = (ImageView) findViewById(R.id.iv_pull_down);
        mReleaseRefreshingView = (ImageView) findViewById(R.id.iv_release_refreshing);
        mPullDownView.setImageResource(cn.bingoogolapple.refreshlayout.R.mipmap.refresh_icon_header_00000);
        mReleaseRefreshingView.setImageResource(mChangeToReleaseRefreshAnimResId);
        headerViewHeight = getResources().getDimensionPixelSize(R.dimen.refresh_header_height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public void onRefresh() {
        stopChangeToReleaseRefreshAd();

        mReleaseRefreshingView.setImageResource(mRefreshingAnimResId);
        mRefreshingAd = (AnimationDrawable) mReleaseRefreshingView.getDrawable();

        mReleaseRefreshingView.setVisibility(VISIBLE);
        mPullDownView.setVisibility(INVISIBLE);

        mRefreshingAd.start();
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        mPullDownView.setVisibility(VISIBLE);
        mReleaseRefreshingView.setVisibility(INVISIBLE);
        // 移动距离小于headerView的实际高度（释放手指的盖度）
        if (y <= headerViewHeight) {
            float scale = y * 1.0f / headerViewHeight;
            handleScale(scale);
        }

    }

    @Override
    public void onRelease() {
        mReleaseRefreshingView.setImageResource(mChangeToReleaseRefreshAnimResId);
        mChangeToReleaseRefreshAd = (AnimationDrawable) mReleaseRefreshingView.getDrawable();

        mReleaseRefreshingView.setVisibility(VISIBLE);
        mPullDownView.setVisibility(INVISIBLE);

        mChangeToReleaseRefreshAd.start();
    }

    @Override
    public void onComplete() {
        stopChangeToReleaseRefreshAd();
        stopRefreshingAd();
    }

    @Override
    public void onReset() {

    }

    private void stopRefreshingAd() {
        if (mRefreshingAd != null) {
            mRefreshingAd.stop();
            mRefreshingAd = null;
        }
    }

    private void stopChangeToReleaseRefreshAd() {
        if (mChangeToReleaseRefreshAd != null) {
            mChangeToReleaseRefreshAd.stop();
            mChangeToReleaseRefreshAd = null;
        }
    }

    public void handleScale(float scale) {
        scale = 0.1f + 0.9f * scale;
        ViewCompat.setScaleX(mPullDownView, scale);
        ViewCompat.setPivotY(mPullDownView, mPullDownView.getHeight());
        ViewCompat.setScaleY(mPullDownView, scale);
    }
}
