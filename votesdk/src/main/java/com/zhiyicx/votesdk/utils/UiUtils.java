package com.zhiyicx.votesdk.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by lei on 2016/8/19.
 */
public class UiUtils {
    private static Toast toast;

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void showShortToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftKeyboard(Context context, View view) {
        if (view == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive())
            inputMethodManager.hideSoftInputFromWindow(
                    view.getWindowToken(), 0);
    }

    public static void setWindowAlpha(Activity context, float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    public static String getStandardTimeWithMinute(long millions) {
        long sencond = millions / 1000;
        String minutes = String.valueOf((int) (sencond / 60));
        String seconds = String.valueOf((int) (sencond % 60));
        if (minutes.length() == 1) minutes = "0" + minutes;
        if (seconds.length() == 1) seconds = "0" + seconds;

        return minutes + ":" + seconds;
    }

    public static long getDeadLineTime(String createTime, long minutes) {
        long create = 0;
        try {
            create = Long.parseLong(createTime) * 1000;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return create + minutes * 60 * 1000 - System.currentTimeMillis();
    }}
