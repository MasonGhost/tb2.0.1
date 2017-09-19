package com.zhiyicx.zhibolibrary.ui.components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jungle on 16/6/3.
 * com.zhiyicx.zhibo.ui.components
 * zhibo_android
 * email:335891510@qq.com
 */
public class InterceptRecycleView extends RecyclerView {
    public InterceptRecycleView(Context context) {
        super(context);
    }

    public InterceptRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }
}
