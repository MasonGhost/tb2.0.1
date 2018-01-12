package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhiyicx.thinksnsplus.widget.listener.OnTouchEventListener;

/**
 * @Author Jliuer
 * @Date 2018/01/12/11:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EnableCheckBox extends android.support.v7.widget.AppCompatCheckBox {

    private OnTouchEventListener mOnTouchEventListener;

    public EnableCheckBox(Context context) {
        super(context);
    }

    public EnableCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnableCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
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
