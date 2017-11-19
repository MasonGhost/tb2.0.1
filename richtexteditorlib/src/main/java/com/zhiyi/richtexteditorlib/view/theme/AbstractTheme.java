package com.zhiyi.richtexteditorlib.view.theme;

import android.os.Parcel;

import com.zhiyi.richtexteditorlib.view.api.ITheme;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractTheme implements ITheme{
    public static final int LIGHT_THEME = 0x01;
    public static final int DARK_THEME = 0x02;


    @Override
    public abstract int[] getBackGroundColors() ;

    @Override
    public abstract int getAccentColor();

    @Override
    public abstract int getNormalColor();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected AbstractTheme(Parcel in) {

    }

}
