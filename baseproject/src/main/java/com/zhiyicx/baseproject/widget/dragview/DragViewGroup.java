package com.zhiyicx.baseproject.widget.dragview;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Looper;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @author LiuChao
 * @describe 作为需要实现弹性滑动的控件父容器，通过ViewDragHelper实现该功能
 * @date 2017/3/24
 * @contact email:450127106@qq.com
 */

public class DragViewGroup extends FrameLayout {
    private static final String TAG = "DragViewGroup";
    public static final int UP_DISTANCE_DEFAULT = 500;// 向上滑动，允许的最大偏移量
    public static final int DOWN_DISTANCE_DEFAULT = 500;// 向下滑动，允许的最大偏移量
    private ViewDragHelper mDragger;
    private DragViewEndgeWatcher mDragView;

    // 纪录原始位置
    private Point mAutoBackOriginPos = new Point();
    private int top = -100;

    // 纪录触摸点，方便判断用户手势
    private float mDownX;
    private float mDownY;

    public DragViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewDragHelper();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean parentDisallowIntercept = true;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 纪录触摸的起始点位置
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dX = ev.getX() - mDownX;
                float dY = ev.getY() - mDownY;
                mDownX = ev.getX();
                mDownY = ev.getY();
                // 判断手指滑动趋势，是偏向水平方向，还是垂直方向
                LogUtils.i(TAG + "ACTION_MOVE  Math.abs(dX)-->" + Math.abs(dX) + "  Math.abs(dY)-->" + Math.abs(dY));
                if (Math.abs(dX) > Math.abs(dY)) {
                    // 偏向水平方向
                } else {
                    // 偏向垂直方向
                    if (dY > 0) {
                        // 向下趋势滑动
                        parentDisallowIntercept = !mDragView.isScrollToTop();
                        LogUtils.i(TAG + "dispatchTouchEvent 向下趋势滑动");
                    } else {
                        //向上趋势滑动
                        parentDisallowIntercept = !mDragView.isScrollToBottom();
                        LogUtils.i(TAG + "dispatchTouchEvent 向上趋势滑动");
                    }
                }
                break;
        }
        LogUtils.i(TAG + "dispatchTouchEvent 不允许父控件接管滑动" + parentDisallowIntercept);
        mDragger.shouldInterceptTouchEvent(ev);
        // requestDisallowInterceptTouchEvent(parentDisallowIntercept);
        return !parentDisallowIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragger.processTouchEvent(ev);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mDragView == null) {
            return;
        }
        mAutoBackOriginPos.x = ((View) mDragView).getLeft();
        mAutoBackOriginPos.y = ((View) mDragView).getTop();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = (DragViewEndgeWatcher) getChildAt(0);
    }

    private void initViewDragHelper() {
        mDragger = ViewDragHelper.create(this, 100.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                LogUtils.i("onViewDragStateChanged" + state);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                DragViewGroup.this.top = top;
                if (top == 0) {
                    LogUtils.i("requestDisallowInterceptTouchEvent(true);------------&&&&&&&" + top);
                }
                LogUtils.i("onViewPositionChanged() called with: changedView = [" + changedView + "], left = [" + left + "], top = [" + top + "], dx = [" + dx + "], dy = [" + dy + "]");
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 水平方向无法拖动
                // 如果需要水平方向拖动，请参照下面的方法进行拓展
                return 0;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                // 假如向上滑动限制为300px,向下滑动限制为200px，那么child的顶部y坐标，就需要在-300 —— 200之间，
                // 也就是这儿的返回值，child的y坐标返回top，但需要在-300--200之间
                // 当然这是child的初始y坐标为0的情况
                return Math.min(Math.max(-UP_DISTANCE_DEFAULT + mAutoBackOriginPos.y, top), DOWN_DISTANCE_DEFAULT - mAutoBackOriginPos.y);
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                // This method should return 0 for views that cannot move vertically.
                // 也就是说子view如果消耗事件，就需要重写该方法，才能够垂直方向移动，这里直接返回child的高度
                return Math.abs(child.getHeight());
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return super.getViewHorizontalDragRange(child);
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                // 回到初始位置
                mDragger.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                invalidate();
            }
        });
    }
}
