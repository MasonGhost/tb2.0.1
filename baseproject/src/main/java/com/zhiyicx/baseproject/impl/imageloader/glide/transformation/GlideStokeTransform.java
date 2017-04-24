package com.zhiyicx.baseproject.impl.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 正方形图片&白色描边
 */
public class GlideStokeTransform implements Transformation<Bitmap> {
    private BitmapPool mBitmapPool;
    private int mStokeWidth = 3;
    private int mWidth;
    private int mHeight;

    public GlideStokeTransform(int stokeWidth) {
        mStokeWidth = stokeWidth;
    }

    public GlideStokeTransform(Context context, int mStokeWidth) {
        this.mStokeWidth = mStokeWidth;
        mBitmapPool = Glide.get(context).getBitmapPool();
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int size = Math.min(source.getWidth(), source.getHeight());

        mWidth = (source.getWidth() - size) / 2;
        mHeight = (source.getHeight() - size) / 2;

        Bitmap.Config config =
                source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;
        Bitmap bitmap = mBitmapPool.get(mWidth, mHeight, config);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(source, mWidth, mHeight, size, size);
        }
        Rect dst = new Rect(mStokeWidth/2, mStokeWidth/2, size - mStokeWidth/2, size - mStokeWidth/2);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mStokeWidth);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(dst, paint);
        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
