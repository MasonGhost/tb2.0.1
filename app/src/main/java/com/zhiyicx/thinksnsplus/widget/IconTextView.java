package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 有待扩展 icon 大小修改
 */
public class IconTextView extends TextView {

    /**
     * 图片资源id
     */
    private int mIconID;
    private Bitmap mBitmap;
    /**
     * 间隔
     */
    private float mSpace;

    /**
     * 原图裁剪区域
     */
    private Rect src;
    /**
     * 处理后目标区域
     */
    private Rect target;

    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_TOP = 2;
    private static final int DIRECTION_RIGHT = 3;
    private static final int DIRECTION_BOTTOM = 4;

    /**
     * 方向
     */
    private int direction = -1;

    public IconTextView(Context context) {
        super(context);
        init(context, null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs,
                    com.zhiyicx.baseproject.R.styleable.IconTextView);
            mIconID = array.getResourceId(com.zhiyicx.baseproject.R.styleable
                    .IconTextView_srcIcon, 0);
            if (mIconID > 0) {
                mBitmap = BitmapFactory.decodeResource(getResources(), mIconID);
                mSpace = array.getDimension(com.zhiyicx.baseproject.R.styleable.IconTextView_space,
                        0);
                direction = array.getInteger(com.zhiyicx.baseproject.R.styleable
                        .IconTextView_direction,
                        DIRECTION_LEFT);
            }
            array.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();
            if (src == null) {
                target = new Rect();
                src = new Rect();
            }
            src.left = 0;
            src.top = 0;
            src.right = width;
            src.bottom = height;
            int textHeight = (int) getTextSize();

            target.left = 0;

            target.top = (int) ((getMeasuredHeight() - getTextSize()) / 2) + 1;
            target.bottom = target.top + textHeight;

            //保证图像不变形
            target.right = (int) (textHeight * (width / (float) height));

            float textLeft = getLayout().getPrimaryHorizontal(0);
            float textWidth = getPaint().measureText(getText().toString());
            float textRight = textWidth + textLeft;


            int iconWidth = target.right - target.left;
            int iconHeight = target.bottom - target.top;

            canvas.save();
            if (direction == DIRECTION_LEFT) {
                canvas.translate(textLeft - target.right - mSpace, 0);
            } else if (direction == DIRECTION_TOP) {
                canvas.translate(textLeft + (textWidth / 2 - iconWidth / 2), target.top - target
                        .bottom
                        - mSpace);
            } else if (direction == DIRECTION_RIGHT) {
                canvas.translate(mSpace + textRight, 0);
            } else if (direction == DIRECTION_BOTTOM) {
                canvas.translate(textLeft + (textWidth / 2 - iconWidth / 2), mSpace + target
                        .bottom -
                        target.top);
            }
            canvas.drawBitmap(mBitmap, src, target, getPaint());
            canvas.restore();
        }
        super.onDraw(canvas);
    }

}
