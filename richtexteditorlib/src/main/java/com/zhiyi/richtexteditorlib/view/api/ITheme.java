package com.zhiyi.richtexteditorlib.view.api;

import android.os.Parcelable;

import java.io.Serializable;
/**
 * @Author Jliuer
 * @Date 17/11/15 13:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface ITheme extends Serializable,Parcelable{
    int[] getBackGroundColors();
    int getAccentColor();
    int getNormalColor();
}
