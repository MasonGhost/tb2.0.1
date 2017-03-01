package com.zhiyicx.baseproject.impl.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
public class GlideMusicBgTransform extends BitmapTransformation {

    private int mStokeWidth = 3;

    public GlideMusicBgTransform(Context context, int mStokeWidth) {
        super(context);
        this.mStokeWidth = mStokeWidth;
    }

    public GlideMusicBgTransform(Context context) {
        super(context);
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
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Rect dst = new Rect(0, 0, size, size);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mStokeWidth);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(squared, 0, 0, null);
        canvas.drawRect(dst, paint);

        return result;
    }
}
