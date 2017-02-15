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
 * @Description 有待扩展 icon 位置方向，目前仅支持左边
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
    private Rect bound ;

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
                    com.zhiyicx.baseproject.R.styleable.topTipView);
            mIconID = array.getResourceId(com.zhiyicx.baseproject.R.styleable
                    .IconTextView_srcIcon, R.mipmap.npc);
            if (mIconID > 0) {
                mBitmap = BitmapFactory.decodeResource(getResources(), mIconID);
            }
//            mSpace = array.getDimension(com.zhiyicx.baseproject.R.styleable.IconTextView_space,
//                    0);
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
                bound = new Rect();
            }
            src.left = 0;
            src.top = 0;
            src.right = width;
            src.bottom = height;
            int textHeight = (int) getTextSize();
            target.left =0;

            target.top = (int) ((getMeasuredHeight() - getTextSize()) / 2) + 1;
            target.bottom = target.top + textHeight;

            //保证图像不变形
            target.right = (int) (textHeight * (width / (float) height));
            canvas.save();
            canvas.translate(getLayout().getPrimaryHorizontal(0)-target.right,0);
            canvas.drawBitmap(mBitmap, src, target, getPaint());
            canvas.restore();
        }
        super.onDraw(canvas);
    }
}
