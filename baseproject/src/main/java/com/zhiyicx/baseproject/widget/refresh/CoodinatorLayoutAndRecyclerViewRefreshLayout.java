package com.zhiyicx.baseproject.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @author LiuChao
 * @describe 处理CoodianatorLayout中ToolBar滚动隐藏后，下拉刷新和recyclerview滑动的冲突
 * @date 2017/4/7
 * @contact email:450127106@qq.com
 */

public class CoodinatorLayoutAndRecyclerViewRefreshLayout extends SwipeToLoadLayout {
    private static final String TAG = "CoodinatorLayoutAndRecy";
    private float downX, downY;
    private int startY;

    public CoodinatorLayoutAndRecyclerViewRefreshLayout(Context context) {
        super(context);
        init();
    }

    public CoodinatorLayoutAndRecyclerViewRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CoodinatorLayoutAndRecyclerViewRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        startY = location[1];
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float temX = ev.getX();
                float temY = ev.getY();
                float dX = temX - downX;
                float dY = temY - downY;
                // 垂直方向 手指向下滑动
                if (Math.abs(dY) / Math.abs(dX) >= 1 && dY > 0) {
                    int[] location = new int[2];
                    getLocationOnScreen(location);
                    LogUtils.i(TAG + "getY" + getY() + "getTop" + getTop() + "location" + location[1]);
                    if (location[1] < startY) {
                        return false;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }
}
