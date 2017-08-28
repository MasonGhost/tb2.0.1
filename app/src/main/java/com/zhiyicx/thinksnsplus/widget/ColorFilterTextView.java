package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author LiuChao
 * @describe 用来设置textView的文字滤镜效果
 * @date 2017/4/12
 * @contact email:450127106@qq.com
 */

public class ColorFilterTextView extends AppCompatTextView {

    public ColorFilterTextView(Context context) {
        super(context);
    }

    public ColorFilterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorFilterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        TextPaint textPaint = getPaint();
        textPaint.setColorFilter(colorFilter);
        invalidate();
    }
}
