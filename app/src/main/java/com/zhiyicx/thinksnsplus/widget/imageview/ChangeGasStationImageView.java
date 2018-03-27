package com.zhiyicx.thinksnsplus.widget.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zhiyicx.common.utils.ConvertUtils;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/27
 * @contact master.jungle68@gmail.com
 */
public class ChangeGasStationImageView extends android.support.v7.widget.AppCompatImageView {
    Path path = new Path();

    public ChangeGasStationImageView(Context context) {
        super(context, null);

    }

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private float[] rids;

    public ChangeGasStationImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    private void init() {
        float rid = ConvertUtils.dp2px(getContext(), 6f);
        //创建圆角数组
        rids = new float[]{rid, rid, rid, rid, 0.0f, 0.0f, 0.0f, 0.0f};
    }

    public ChangeGasStationImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {


        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = this.getWidth();
        int h = this.getHeight();
        path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}