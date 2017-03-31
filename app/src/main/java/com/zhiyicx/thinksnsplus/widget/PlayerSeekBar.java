package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;


/**
 * @Author Jliuer
 * @Date 2017/3/8/14:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PlayerSeekBar extends SeekBar {

    private boolean drawLoading = false;
    private int degree = 0;
    private Matrix matrix = new Matrix();
    private Bitmap loading = BitmapFactory.decodeResource(getResources(), R.drawable
            .play_plybar_btn_loading);
    private Drawable drawable;

    public PlayerSeekBar(Context context) {
        super(context);
    }

    public PlayerSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setThumb(getContext().getResources().getDrawable(R.mipmap.music_pic_progressbar_circle));
    }

    public void setLoading(boolean loading) {
        drawLoading = loading;
        if (drawLoading) {
            invalidate();
        }
    }

    @Override
    public void setThumb(Drawable thumb) {
        Rect localRect = null;
        if (drawable != null) {
            localRect = drawable.getBounds();
        }
        super.setThumb(drawable);
        drawable = thumb;
        if ((localRect != null) && (drawable != null)) {
            drawable.setBounds(localRect);
        }
    }

    @Override
    public Drawable getThumb() {
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getThumb();
        }
        return drawable;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawLoading) {
            canvas.save();
            degree = ((int) (degree + 3.0F));
            degree %= 360;
            matrix.reset();
            matrix.setScale(0.3f, 0.3f, loading.getWidth() / 2, loading.getHeight() / 2);
            matrix.postRotate(degree, loading.getWidth() / 2, loading.getHeight() / 2);
            canvas.translate(getPaddingLeft() + getThumb().getBounds().left + getThumb().getBounds
                            ().width() / 2 - loading.getWidth() / 2 - getThumbOffset(),
                    getPaddingTop() + getThumb().getBounds().top + getThumb().getBounds().height()
                            / 2 - loading.getHeight() / 2);
            canvas.drawBitmap(loading, matrix, null);
            canvas.restore();
            invalidate();
        }

    }
}
