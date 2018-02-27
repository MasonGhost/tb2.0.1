package com.zhiyicx.baseproject.widget.button;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * @author Jungle68
 * @describe
 * @date 2018/2/27
 * @contact master.jungle68@gmail.com
 */
public class ProgressButton extends android.support.v7.widget.AppCompatButton {
    private Paint mPaint = new Paint();
    private float mCorner = 100f;
    private int mBackgroundColor = Color.TRANSPARENT;
    private int mForcegroundColor = Color.BLUE;
    private float mProgress = 0;
    private int mTextColor = Color.WHITE;
    private float mTextSize = 20;
    private float mMax = 100;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //开始画背景图片
        RectF oval = new RectF(0, 0, getWidth(), getHeight());
        mPaint.setColor(mBackgroundColor);
        canvas.drawRoundRect(oval, mCorner, mCorner, mPaint);
        //开始画里面的进度条   坐标是  左上角  右下角
        mPaint.setColor(mForcegroundColor);
        if (mProgress < mCorner) {
            oval = new RectF(0, mCorner - mProgress, (getWidth() * mProgress) / mMax, getHeight());
            canvas.drawRoundRect(oval, mProgress, mProgress, mPaint);
        } else {
            oval = new RectF(0, 0, (getWidth() * mProgress) / mMax, getHeight());
            canvas.drawRoundRect(oval, mCorner, mCorner, mPaint);
        }

        //开始画图片中间的文字
        if (getText() == null) {
            return;
        }
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);

        float[] widths = new float[getText().length() - 1];
        mPaint.getTextWidths(getText(), 0, getText().length() - 1, widths);

        float textWidth = 0;
        for (int i = 0; i < widths.length; i++) {
            textWidth += widths[i];
        }
        float textHeightinVer = getHeight() / 2 - mPaint.descent() + (mPaint.descent() - mPaint.ascent()) / 2;
        canvas.drawText(getText().toString(), getWidth() / 2 - textWidth / widths.length, textHeightinVer, mPaint);
    }

    /**
     * 设置进度条的值
     */
    public void setProgress(int progress) {
        if (progress > mMax) {
            mProgress = mMax;
        }
        this.mProgress = progress;
        //强制刷新界面，绘制新的进度，此时会调用onDraw方法
        postInvalidate();
    }

}
