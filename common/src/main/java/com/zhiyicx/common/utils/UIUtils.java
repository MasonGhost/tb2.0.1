package com.zhiyicx.common.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.zhiyicx.common.base.BaseApplication;

/**
 * @author LiuChao
 * @describe 一些和Ui相关的工具类方法
 * @date 2017/1/10
 * @contact email:450127106@qq.com
 */

public class UIUtils {
    /**
     * TextView设置下划线
     */
    public static void setBottomDivider(TextView textView) {
        if (textView == null) {
            return;
        }
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

    /**
     * Textview或者button设置Drawable
     */
    public static Drawable getCompoundDrawables(Context context, int imgRsID) {
        if (imgRsID == 0) {
            return null;
        }
        Drawable drawable = ContextCompat.getDrawable(context, imgRsID);
        if (drawable == null) {
            return null;
        }
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 通过资源名称查找resource
     */
    public static int getResourceByName(String name, String type, Context context) {
        int id = context.getResources().getIdentifier(name, type, context.getPackageName());
        return id;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */

    public static int getWindowHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
