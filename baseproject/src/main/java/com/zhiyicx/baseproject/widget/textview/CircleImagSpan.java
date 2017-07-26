package com.zhiyicx.baseproject.widget.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleImagSpan extends CenterImageSpan {

    public CircleImagSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public CircleImagSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public CircleImagSpan(Drawable d) {
        super(d);
    }
}
