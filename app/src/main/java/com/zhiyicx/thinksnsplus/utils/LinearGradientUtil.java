package com.zhiyicx.thinksnsplus.utils;

import android.graphics.Color;

/**
 * @author Jungle68
 * @describe 获取渐变色的值
 * @date 2018/3/6
 * @contact master.jungle68@gmail.com
 */
public class LinearGradientUtil {
    private int mStartColor;
    private int mEndColor;

    public LinearGradientUtil(int startColor, int endColor) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
    }


    //获取某一个百分比间的颜色,radio取值[0,1]
    public int getColor(float radio) {
        int redStart = Color.red(mStartColor);
        int blueStart = Color.blue(mStartColor);
        int greenStart = Color.green(mStartColor);
        int redEnd = Color.red(mEndColor);
        int blueEnd = Color.blue(mEndColor);
        int greenEnd = Color.green(mEndColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }
}
