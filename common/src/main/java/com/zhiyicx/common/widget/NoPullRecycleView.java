package com.zhiyicx.common.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class NoPullRecycleView extends RecyclerView {

    public NoPullRecycleView(Context context) {
        super(context);
    }


    public NoPullRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }

}
