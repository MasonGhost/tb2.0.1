package com.zhiyicx.zhibolibrary.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by jess on 7/9/16.
 */
public class CountImageView extends ImageView {

    private Paint mPaint;
    private boolean isVisiable = true;
    private int mWidth;
    private int mHeight;
    private Paint mTextPaint;
    private int mDotRadius;
    private float mTxtHeight;
    private float mTxtWidth;
    private int mCount;
    private float mCenterX;
    private float mCenterY;
    public static final int COUNT_TOP = 99;

    public CountImageView(Context context) {
        this(context, null);
    }

    public CountImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDotRadius = AutoUtils.getPercentWidthSize(12);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(UiUtils.getColor(R.color.color_dot));
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setARGB(255, 255, 255, 255);
        mTextPaint.setTextSize((float) (mDotRadius * 1.4f));
        mTextPaint.setColor(UiUtils.getColor(R.color.white));

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (float) Math.ceil(fm.descent - fm.ascent);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        mCenterX = mWidth / 2 + mWidth / 3;
        mCenterY = mHeight / 2 - mHeight / 3;

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isVisiable) {
            if (mCount <= 0) {
                return;
            }
            canvas.drawCircle(mCenterX, mCenterY, mDotRadius, mPaint);
            String count = Math.min(mCount, COUNT_TOP) + "";//不能超过设定的上限
            mTxtWidth = mTextPaint.measureText(count, 0, count.length());
            canvas.drawText(count, mCenterX - mTxtWidth / 2, mCenterY + mTxtHeight / 4, mTextPaint);

        }
    }


    public void setCount(int count) {
        this.mCount = count;
        postInvalidate();
    }

    /**
     * 设置红点提示是否可见
     *
     * @param isVisiable
     */
    public void setDotVisiable(boolean isVisiable) {
        this.isVisiable = isVisiable;
        postInvalidate();//重绘
    }
}
