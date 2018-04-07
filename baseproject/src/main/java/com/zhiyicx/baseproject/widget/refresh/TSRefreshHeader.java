package com.zhiyicx.baseproject.widget.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.header.internal.MaterialProgressDrawable;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.zhiyicx.baseproject.R;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */
public class TSRefreshHeader extends FrameLayout implements RefreshHeader {
    private ImageView mReleaseRefreshingView;
    private MaterialProgressDrawable mProgress;
    private int mChangeToReleaseRefreshAnimResId;

    public TSRefreshHeader(@NonNull Context context) {
        this(context,null);
    }

    public TSRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TSRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mReleaseRefreshingView=new ImageView(getContext());
        /*mChangeToReleaseRefreshAnimResId = R.drawable.refresh_loading;
        mReleaseRefreshingView=new ImageView(getContext());
        mReleaseRefreshingView.setImageResource(mChangeToReleaseRefreshAnimResId);*/

        mProgress = new MaterialProgressDrawable(getContext(),mReleaseRefreshingView);
        mProgress.showArrow(true);
        mProgress.setAlpha(255);
        mProgress.setStartEndTrim(0f, 0.8f);
        mProgress.setArrowScale(1f); //0~1之间
        mProgress.setProgressRotation(1);
        mProgress.setColorSchemeColors(getResources().getColor(R.color.themeColor));
        mReleaseRefreshingView.setImageDrawable(mProgress);

        addView(mReleaseRefreshingView);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) mReleaseRefreshingView.getLayoutParams();
        layoutParams.height=getResources().getDimensionPixelOffset(R.dimen.refresh_header_height);
        layoutParams.width=getResources().getDimensionPixelOffset(R.dimen.refresh_header_with);
        layoutParams.gravity= Gravity.CENTER;
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        mProgress.showArrow(true);
        mProgress.setAlpha(255);
        mProgress.setStartEndTrim(0f, 0.8f);
        mProgress.setArrowScale(1f);
        mProgress.setProgressRotation(1);
    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Scale;
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {
        setBackgroundColor(colors[0]);
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        /*AnimationDrawable background = (AnimationDrawable) mReleaseRefreshingView.getDrawable();
        if (!background.isRunning()) {
            background.start();
        }*/
        mProgress.start();
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        if(success){
            // 刷新成功
        }else {
            // 刷新失败
        }
        /*AnimationDrawable background = (AnimationDrawable) mReleaseRefreshingView.getDrawable();
        if (background.isRunning()) {
            background.stop();
        }*/
        mProgress.stop();
        return 300;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
}
