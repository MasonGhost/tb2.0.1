package com.zhiyicx.baseproject.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.SkinUtils;


/**
 * @Describe show #mPressedColor color when pressed
 * @Author Jungle68
 * @Date 2017/2/29
 * @Contact master.jungle68@gmail.com
 */
public class FilterImageView extends ImageView {
    private static final int SHAPE_SQUARE = 0;
    private static final int SHAPE_CIRLCE = 1;
    private static final int DEFAULT_PRESSED_COLOR = 0x26000000; // cover：#000000 alpha 15%
    private int mPressedColor = DEFAULT_PRESSED_COLOR;// pressed color
    private int mShape = SHAPE_SQUARE;
    private Paint mPaint;
    private boolean isText;
    private Rect mRect;

    private Bitmap mBitmap;
    private boolean mIshowLongTag;

    private boolean mUseNumberShadow;
    private String mNumber;

    public FilterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }


    public FilterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public FilterImageView(Context context) {
        super(context);
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FilterImageView);
        mPressedColor = array.getInteger(R.styleable.FilterImageView_pressColor, DEFAULT_PRESSED_COLOR);
        mShape = array.getInteger(R.styleable.FilterImageView_pressShape, SHAPE_SQUARE);
        array.recycle();
        mPaint = new TextPaint();
        mPaint.setColor(mPressedColor);
        mPaint.setAntiAlias(true);
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPressed()) {
            switch (mShape) {
                // square
                case SHAPE_SQUARE:
                    canvas.drawColor(mPressedColor);
                    break;
                // circle
                case SHAPE_CIRLCE:
                    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
                    break;
                default:

            }
        }
        if (isText) {
            mPaint.setColor(SkinUtils.getColor(R.color.general_for_hint));
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
            mPaint.setTextSize(getWidth() / 2);
            mPaint.setColor(Color.WHITE);
            canvas.drawText("匿", getWidth() / 2 - mPaint.measureText("匿") / 2, getHeight() / 2 - (mPaint.descent() + mPaint.ascent()) / 2, mPaint);
        }
        if (mIshowLongTag && mBitmap != null) {
            canvas.drawBitmap(mBitmap, getWidth() - mBitmap.getWidth(), getHeight() - mBitmap.getHeight(), null);
        }
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
        invalidate();
    }

    public void setIsText(boolean isText) {
        this.isText = isText;
        postInvalidate();
    }

    public void setUseNumberShadow(boolean useNumberShadow,int num) {
        mUseNumberShadow = useNumberShadow;
        mNumber=String.valueOf(num);
        postInvalidate();
    }

    public void showLongImageTag(boolean isShow) {
        this.mIshowLongTag = isShow;
        if (isShow) {
            if (mBitmap == null) {
                mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_longpic);
            }
            postInvalidate();
        }
    }
}
