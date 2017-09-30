package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author legendary_tym
 * @data 2015.11.22 00.23
 * @Description 强制宽高相等，功能有待扩展
 */
public class CropSquareImageView extends android.support.v7.widget.AppCompatImageView {

    public CropSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CropSquareImageView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //强制宽高相等
        int size = Math.min(widthMeasureSpec, widthMeasureSpec);
        super.onMeasure(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}