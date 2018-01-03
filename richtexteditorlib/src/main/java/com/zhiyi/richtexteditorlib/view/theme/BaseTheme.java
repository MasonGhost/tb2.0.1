package com.zhiyi.richtexteditorlib.view.theme;

import android.graphics.Color;
import android.os.Parcel;

@SuppressWarnings("WeakerAccess")
public class BaseTheme extends AbstractTheme{


    public static final Creator<BaseTheme> CREATOR = new Creator<BaseTheme>() {
        @Override
        public BaseTheme createFromParcel(Parcel in) {
            return new BaseTheme(in);
        }

        @Override
        public BaseTheme[] newArray(int size) {
            return new BaseTheme[size];
        }
    };

    public BaseTheme(){
        super(null);
    }

    protected BaseTheme(Parcel in) {
        super(in);
    }

    @Override
    public int[] getBackGroundColors() {
        return new int[] {};
    }

    @Override
    public int getAccentColor() {
        return Color.parseColor("#59b6d7");
    }

    @Override
    public int getNormalColor() {
        return Color.GRAY;
    }
}
