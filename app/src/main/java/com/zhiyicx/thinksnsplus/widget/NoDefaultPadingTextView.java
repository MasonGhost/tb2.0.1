package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @Author Jliuer
 * @Date 2017/03/09
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class NoDefaultPadingTextView extends AppCompatTextView {

    Paint.FontMetricsInt fontMetricsInt;

    public NoDefaultPadingTextView(Context context) {
        this(context, null);
    }

    public NoDefaultPadingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoDefaultPadingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (fontMetricsInt == null) {
            fontMetricsInt = new Paint.FontMetricsInt();
            getPaint().getFontMetricsInt(fontMetricsInt);
        }
        canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent-2);
        super.onDraw(canvas);
    }
}
