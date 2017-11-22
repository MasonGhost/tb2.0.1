package com.zhiyicx.zhibolibrary.ui.components;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jess on 2015/11/23.
 */
public class NoPullViewPager  extends ViewPager{

    public NoPullViewPager(Context context) {
        super(context);
    }



    public NoPullViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }

}
