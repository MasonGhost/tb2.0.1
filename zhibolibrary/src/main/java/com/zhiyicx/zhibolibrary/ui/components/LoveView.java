package com.zhiyicx.zhibolibrary.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.Random;

/**
 * Created by jess on 16/5/17.
 */
public class LoveView extends View {

    private Paint paint;
    private float rate = 2f; // 半径变化率
    private Path path; // 路径
    private Random random = new Random();//用于实现随机功能
    private int mColor = Color.argb(255,random.nextInt(200)+50,random.nextInt(200)+50,random.nextInt(200)+50);
    private int mWidth = AutoUtils.getPercentWidthSize(100);
    private int mHeight = AutoUtils.getPercentWidthSize(100);

    public LoveView(Context context) {
        this(context, null);
    }

    public LoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        // 创建一个路径
        path = new Path();
    }

    public void setColor(int color){
        this.mColor = color;
        paint.setColor(mColor);
    }

    public void setWidthAndHeight(int height, int width) {
        this.mHeight = AutoUtils.getPercentHeightSize(height);
        this.mWidth = AutoUtils.getPercentWidthSize(width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 重置画板
        path.reset();
        // 得到屏幕的长宽的一半
        int px = mWidth / 2;
        int py = mHeight / 2;
        // 路径的起始点
        path.moveTo(px, py - 5 * rate);
        // 根据心形函数画图
        for (double i = 0; i <= 2 * Math.PI; i += 0.001) {
            float x = (float) (16 * Math.sin(i) * Math.sin(i) * Math.sin(i));
            float y = (float) (13 * Math.cos(i) - 5 * Math.cos(2 * i) - 2 * Math.cos(3 * i) - Math.cos(4 * i));
            x *= rate;
            y *= rate;
            x = px - x;
            y = py - y;
            path.lineTo(x, y);
        }
        canvas.drawPath(path, paint);
    }


}