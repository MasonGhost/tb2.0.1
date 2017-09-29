package com.zhiyicx.baseproject.impl.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


/**
 * @Describe 添加圆形 transform
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */
public class GlideCircleTransform extends BitmapTransformation {

    private Paint mPaint;

    public GlideCircleTransform(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
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
        Canvas mCanvas=new Canvas(result);
        float r = size / 2f;
        //画图片
        mPaint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        mCanvas.drawCircle(r, r, r - 0.1f, mPaint);

        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
