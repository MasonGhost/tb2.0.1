package com.zhiyicx.zhibolibrary.ui.Transformation;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zhiyicx.zhibolibrary.util.FastBlur;

/**
 * Created by zhiyicx on 2016/4/1.
 */
public class GaussianBlurTrasform extends BitmapTransformation {
    public GaussianBlurTrasform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return FastBlur.blurBitmap(toTransform,outWidth,outHeight);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
