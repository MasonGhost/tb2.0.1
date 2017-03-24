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
import android.widget.ScrollView;

import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/24
 * @contact email:450127106@qq.com
 */

public class DragScrollView extends ScrollView {
    private static final String TAG = "DragScrollView";
    private ViewDragHelper mDragger;
    private View mDragView;
    private int max_distance;// 最大位移距离
    // 纪录原始位置
    private Point mAutoBackOriginPos = new Point();

    public DragScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        max_distance = DeviceUtils.getScreenHeight(context) / 2;
        initViewDragHelper();
    }

    float lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float curY = event.getY();
                int deltaY = (int) (curY - lastY);
                lastY = curY;

                if (deltaY < 0 && isScrollToBottom()) {
                    return mDragger.shouldInterceptTouchEvent(event);
                }
                if (deltaY > 0&&isScrollToTop()) {
                    return mDragger.shouldInterceptTouchEvent(event);
                }
                break;
        }
        //return mDragger.shouldInterceptTouchEvent(event);
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // mDragger.processTouchEvent(event);
        //return true;
        return super.onTouchEvent(event);
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
        mAutoBackOriginPos.x = mDragView.getLeft();
        mAutoBackOriginPos.y = mDragView.getTop();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // scrollview的子控件，第一个就是所有内容
        mDragView = getChildAt(0);
    }

    private void initViewDragHelper() {
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 水平方向无法拖动
                return 0;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (Math.abs(top) > max_distance) {
                    // 超过最大限制距离，就不能够继续滑动
                    return max_distance;
                }
                return top;
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

    /*
  * 判断是否划动到了底部
  */
    private boolean isScrollToBottom() {
        return getScrollY() + getHeight() >= computeVerticalScrollRange();
    }

    private boolean isScrollToTop() {
        return getScrollY() <= 0;
    }


}
