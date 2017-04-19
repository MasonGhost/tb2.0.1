package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 嵌套滑动, 约定头部 id 必须是 R.id.nestedscroll_target
 */
public class NestedScrollLineayLayout extends LinearLayout implements NestedScrollingParent {

    private NestedScrollingParentHelper parentHelper;
    private View headerView;
    private int mTopViewHeight;
    private int mNotConsumeHeight;
    private OverScroller mScroller;
    private boolean addHeight;
    private OnHeadFlingListener mOnHeadFlingListener;
    private boolean hiddenTop;
    private boolean showTop;
    // 阻尼系数
    private static final float SCROLL_RATIO = 0.5f;
    private float mPreX, mPreY, mDistanceY;

    public NestedScrollLineayLayout(Context context) {
        super(context);
        init(context);
    }

    public NestedScrollLineayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NestedScrollLineayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.setOrientation(LinearLayout.VERTICAL);
        parentHelper = new NestedScrollingParentHelper(this);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPreX = ev.getX();
//                mPreY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mDistanceY = ev.getY() - mPreY;
//                LogUtils.d("onStopNestedScroll");
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreX = ev.getX();
                mPreY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mDistanceY = ev.getY() - mPreY;
                LogUtils.d("onTouchEvent");
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断HeadView是否完全显示了.
     *
     * @return true, 完全显示, false 没有显示
     */
    private boolean isDisplayHeaderView() {
        int[] location = new int[2]; // 0位存储的是x轴的值, 1是y轴的值
        // 获取HeadView屏幕中y轴的值
        headerView.getLocationOnScreen(location);
        int mSecondHeaderViewYOnScreen = location[1];
        return mSecondHeaderViewYOnScreen > 0 ? true : false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (headerView == null) {
            headerView = findViewById(R.id.nestedscroll_target);
        }
        if (headerView == null) {
            throw new RuntimeException("headerView can not be null");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = headerView.getMeasuredHeight();
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        parentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onStopNestedScroll(View child) {
        parentHelper.onStopNestedScroll(child);
        LogUtils.d("onStopNestedScroll");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //处理子view传上来的事件
        //头部高度
        mTopViewHeight = headerView.getHeight() - mNotConsumeHeight;
        hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target,
                -1);
        if (hiddenTop || showTop) {
            if (!addHeight) {//只增加一次 高度 height
                addHeight = true;
                ViewGroup.LayoutParams params = this.getLayoutParams();
                params.height = mTopViewHeight + this.getHeight();
                this.setLayoutParams(params);
                requestLayout();
            }
            scrollBy(0, dy);
            consumed[1] = dy;
        }
        if (mOnHeadFlingListener != null && getScrollY() <= mTopViewHeight) {
            mOnHeadFlingListener.onHeadFling(getScrollY());
        }
        LogUtils.d("onNestedPreScroll:::");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int
            dyUnconsumed) {
//        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {

        int scrollY = getScrollY();

        if (getScrollY() >= mTopViewHeight) {
            return false;
        }
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        if (hiddenTop) {
            LogUtils.d("hiddenTop");
            mScroller.fling(0, scrollY, (int) velocityX, (int) velocityY, 0, 0, 0,
                    mTopViewHeight);
            mOnHeadFlingListener.onHeadFling(mTopViewHeight);
        } else if (showTop) {
            LogUtils.d("showTop");
            mScroller.fling(0, scrollY, (int) velocityX, (int) velocityY, 0, 0, 0,
                    -mTopViewHeight);
            mOnHeadFlingListener.onHeadFling(0);
        }
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public void setOnHeadFlingListener(OnHeadFlingListener onHeadFlingListener) {
        mOnHeadFlingListener = onHeadFlingListener;
    }

    public int getTopViewHeight() {
        return mTopViewHeight;
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setNotConsumeHeight(int notConsumeHeight) {
        mNotConsumeHeight = notConsumeHeight;
    }

    public interface OnHeadFlingListener {
        void onHeadFling(int scrollY);
    }
}
