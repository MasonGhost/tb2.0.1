package com.zhiyicx.baseproject.widget.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * @Author Jliuer
 * @Date 2017/07/15
 * @Email Jliuer@aliyun.com
 * @Description 控制图片位置
 */
public class CenterImageSpan extends ImageSpan {

    public CenterImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public CenterImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public CenterImageSpan(Drawable d) {
        super(d);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt
            fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY = 0;// y 轴居中对齐
        transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
        canvas.translate(x + 4, transY);// x+4 个偏移，不贴紧文字
        b.draw(canvas);
        canvas.restore();
    }
}
