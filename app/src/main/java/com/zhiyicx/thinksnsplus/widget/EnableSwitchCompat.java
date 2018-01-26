package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhiyicx.thinksnsplus.widget.listener.OnTouchEventListener;

/**
 * @Author Jliuer
 * @Date 2018/01/12/11:19
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EnableSwitchCompat extends SwitchCompat {

    private OnTouchEventListener mOnTouchEventListener;

    public EnableSwitchCompat(Context context) {
        super(context);
    }

    public EnableSwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnableSwitchCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mOnTouchEventListener != null) {
            return super.onTouchEvent(ev) && mOnTouchEventListener.onTouchEvent(isEnabled());
        } else {
            return super.onTouchEvent(ev);
        }
    }

    public OnTouchEventListener getOnTouchEventListener() {
        return mOnTouchEventListener;
    }

    public void setOnTouchEventListener(OnTouchEventListener onTouchEventListener) {
        mOnTouchEventListener = onTouchEventListener;
    }
}
