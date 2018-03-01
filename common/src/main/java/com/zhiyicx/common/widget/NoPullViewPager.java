package com.zhiyicx.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class NoPullViewPager extends ViewPager {

    private boolean mCanScroll;

    public NoPullViewPager(Context context) {
        super(context);
    }


    public NoPullViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!mCanScroll) {
            return false;
        }
        return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!mCanScroll) {
            return false;
        }
        return super.onInterceptTouchEvent(arg0);
    }

    public void setCanScroll(boolean canScroll) {
        mCanScroll = canScroll;
    }

}

