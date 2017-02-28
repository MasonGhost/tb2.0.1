package com.zhiyicx.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Describe 自定义imageview,，增加图片按下蒙层效果
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */

public class FilterImageView extends ImageView {

    public FilterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

    public FilterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
    }

    public FilterImageView(Context context) {
        super(context);
//        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPressed()) {
            Paint paint=new Paint();
            paint.setColor(0x33000000);
            canvas.drawCircle(canvas.getClipBounds().centerX(), canvas.getClipBounds().centerY(),canvas.getWidth()/2,paint);
        }

    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
        invalidate();
    }

//    private void init() {
//        setOnTouchListener(onTouchListener);
//    }
//    private OnTouchListener onTouchListener=new OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_UP:// 1
//                    setColorFilter(null);
//                    break;
//                case MotionEvent.ACTION_DOWN:// 0
//                    changeLight();
//                    break;
//                case MotionEvent.ACTION_MOVE:// 2
//                    break;
//                case MotionEvent.ACTION_CANCEL:// 3
//                    setColorFilter(null);
//                    break;
//                default:
//                    break;
//            }
//            return false;
//        }
//    };
//    private void changeLight() {
//        int brightness=-50;
//        ColorMatrix matrix = new ColorMatrix();
//        matrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
//                brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
//        setColorFilter(new ColorMatrixColorFilter(matrix));
//
//    }
}
