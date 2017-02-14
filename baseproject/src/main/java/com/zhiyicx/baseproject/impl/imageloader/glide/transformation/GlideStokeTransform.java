package com.zhiyicx.baseproject.impl.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 正方形图片&白色描边
 */
public class GlideStokeTransform extends BitmapTransformation {

    private int mStokeWidth;

    public GlideStokeTransform(Context context, int mStokeWidth) {
        super(context);
        this.mStokeWidth = mStokeWidth;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return dealStoke(pool, toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    private Bitmap dealStoke(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());

        Bitmap result = pool.get(size + 2 * mStokeWidth, size + 2 * mStokeWidth, Bitmap.Config
                .ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size + 2 * mStokeWidth, size + 2 * mStokeWidth, Bitmap
                    .Config.ARGB_8888);
        }
        Rect src = new Rect(0, 0, source.getWidth(), source.getHeight());
        Rect dst = new Rect(0, 0, size, size);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mStokeWidth);
        paint.setColor(0xffffff);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, result.getWidth(), result.getHeight(), paint);
        canvas.drawBitmap(source, src, dst, null);
        return result;
    }
}
