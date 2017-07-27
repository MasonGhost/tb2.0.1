package com.zhiyicx.baseproject.widget.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.ImageSpan;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.SkinUtils;

/**
 * @Author Jliuer
 * @Date 2017/07/15
 * @Email Jliuer@aliyun.com
 * @Description 控制图片位置
 */
public class CenterImageSpan extends ImageSpan {

    String text = "匿";
    boolean isText;

    public CenterImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public CenterImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public CenterImageSpan(Drawable d) {
        super(d);
    }

    public CenterImageSpan(Drawable d, boolean isText) {
        super(d);
        this.isText = isText;
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
        return rect.right + 20;// x 偏移，不贴紧文字
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        if (isText) {
            canvas.drawCircle(b.getBounds().centerX(), b.getBounds().centerY(), b.getBounds().right - b.getBounds().centerX(), paint);
            Paint textP = new TextPaint(paint);
            Paint textB = new TextPaint(paint);
            textB.setColor(SkinUtils.getColor(R.color.qa_niming));
            textP.setColor(Color.WHITE);
            textP.setTextSize(ConvertUtils.sp2px(BaseApplication.getContext(),12));
            canvas.drawText("匿", b.getBounds().centerX() - textP.measureText("匿") / 2, b.getBounds().centerY() - (textP.descent() + textP.ascent()) / 2, textP);
        } else {
            canvas.save();
            int transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;// y 轴居中对齐
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }


    }
}
