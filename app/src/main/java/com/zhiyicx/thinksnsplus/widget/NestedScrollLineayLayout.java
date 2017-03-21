package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

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
                params.height = mTopViewHeight + this.getHeight() - 120;
                this.setLayoutParams(params);
                requestLayout();
            }
            scrollBy(0, dy);
            consumed[1] = dy;
        }
        if (mOnHeadFlingListener != null && getScrollY() <= mTopViewHeight) {
            mOnHeadFlingListener.onHeadFling(getScrollY());
        }

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int
            dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() >= mTopViewHeight) {
            return false;
        }
        if (hiddenTop) {
            mOnHeadFlingListener.onHeadFling(mTopViewHeight);
        } else {
            mOnHeadFlingListener.onHeadFling(0);
        }
        fling((int) velocityY);
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

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
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
