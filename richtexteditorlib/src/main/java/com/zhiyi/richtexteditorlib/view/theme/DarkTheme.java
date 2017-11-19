package com.zhiyi.richtexteditorlib.view.theme;

import android.graphics.Color;

public class DarkTheme extends BaseTheme {

    @Override
    public int[] getBackGroundColors() {
        return new int[]{Color.DKGRAY,Color.rgb(50,50,50)};
    }

    @Override
    public int getAccentColor() {
        return Color.rgb(255,161,118);
    }

    @Override
    public int getNormalColor() {
        return Color.LTGRAY;
    }
}
