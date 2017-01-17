package com.zhiyicx.common.widget;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * @Describe  自定义imageview,，增加图片按下蒙层效果
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */

public class FilterImageView extends ImageView {

    public FilterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FilterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FilterImageView(Context context) {
        super(context);
        init();
    }


    private void init() {
        setOnTouchListener(onTouchListener);
    }
    private OnTouchListener onTouchListener=new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:// 1
                    setColorFilter(null);
                    break;
                case MotionEvent.ACTION_DOWN:// 0
                    changeLight();
                    break;
                case MotionEvent.ACTION_MOVE:// 2
                    break;
                case MotionEvent.ACTION_CANCEL:// 3
                    setColorFilter(null);
                    break;
                default:
                    break;
            }
            System.out.println("event = " + event.getAction());
            return false;
        }
    };
    private void changeLight() {
        int brightness=-80;
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
                brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
        setColorFilter(new ColorMatrixColorFilter(matrix));

    }
}
