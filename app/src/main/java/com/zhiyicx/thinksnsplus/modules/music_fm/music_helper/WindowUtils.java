package com.zhiyicx.thinksnsplus.modules.music_fm.music_helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.zycx.spicycommunity.R;
import com.zycx.spicycommunity.widget.TFrameAnimationSuf;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * @author legendary_tym
 * @Title SpicyCommunity
 * @Package com.zycx.spicycommunity.utils
 * @Description: 加载弹窗, dialog似乎必须依附于contextwrapper。
 * 待优化、单例&判断view
 * @date: 2016-11-22 11:46
 */

public class WindowUtils {
    private static final String LOG_TAG = "WindowUtils";
    private static View mView = null;
    private static WindowManager mWindowManager = null;
    private static Context mContext = null;

    private static Boolean isShown = false;

    private static TFrameAnimationSuf frameView;

    private static TextView textView;//测试使用

    private static OnWindowDismisslistener windowDismisslistener;

    public interface OnWindowDismisslistener {
        void onDismiss();
    }

    public static void setWindowDismisslistener(OnWindowDismisslistener windowDismisslistener) {
        WindowUtils.windowDismisslistener = windowDismisslistener;
    }

    public static void showPopupWindow(final Context context, View view) {
        if (isShown) {
            LogUtil.iLog(LOG_TAG, "return cause already shown");
            return;
        }

        isShown = true;
        // 获取应用的Context
        mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mView = setUpView(context, "");
        final LayoutParams params = new LayoutParams();

        String packname = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android" +
                ".permission.SYSTEM_ALERT_WINDOW", packname));
        if (permission) {
            params.type = LayoutParams.TYPE_PHONE;
        } else {
            params.type = LayoutParams.TYPE_TOAST;
        }
        // 设置flag
        int flags = LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;

        params.gravity = Gravity.CENTER;

        if (frameView != null) frameView.start();

        mWindowManager.addView(mView, params);

    }

    public static void showPopupWindow(final Context context) {
        if (isShown) {
            LogUtil.iLog(LOG_TAG, "return cause already shown");
            return;
        }

        isShown = true;
        // 获取应用的Context
        mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mView = setUpView(context, "");
        final LayoutParams params = new LayoutParams();
        String packname = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android" +
                ".permission.SYSTEM_ALERT_WINDOW", packname));
        if (permission) {
            params.type = LayoutParams.TYPE_PHONE;
        } else {
            params.type = LayoutParams.TYPE_TOAST;
        }

        // 设置flag

        int flags = LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;

        params.gravity = Gravity.CENTER;

        if (frameView != null) frameView.start();

        mWindowManager.addView(mView, params);

    }

    public static void showPopupWindowE(Context context, String str) {
        if (isShown) {
            LogUtil.iLog(LOG_TAG, "return cause already shown");
            return;
        }

        isShown = true;
        // 获取WindowManager
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mView = setUpView(context, str);
        final LayoutParams params = new LayoutParams();
        String packname = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android" +
                ".permission.SYSTEM_ALERT_WINDOW", packname));
        if (permission) {
            params.type = LayoutParams.TYPE_PHONE;
        } else {
            params.type = LayoutParams.TYPE_TOAST;
        }

        // 设置flag

        int flags = LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;

        params.gravity = Gravity.CENTER;

        if (frameView != null) frameView.start();

        mWindowManager.addView(mView, params);

    }

    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
        if (isShown && null != mView) {
            if (windowDismisslistener != null) {
                windowDismisslistener.onDismiss();
            }
//            if (frameView != null) frameView.stop();
            mWindowManager.removeView(mView);
            isShown = false;
        }

    }

    private static View setUpView(final Context context, String str) {

        View defaultView = LayoutInflater.from(context).inflate(R.layout.dialog_load,
                null);
        frameView = (TFrameAnimationSuf) defaultView.findViewById(R.id.dialog_progress);
        frameView.setBitmapResoursID(TFrameAnimationSuf.srcId);
        frameView.setGapTime(100);
//        }

        if (!TextUtils.isEmpty(str)) {
            frameView.setVisibility(View.GONE);
            textView = (TextView) defaultView.findViewById(R.id.dialog_window_tv);
            textView.setText(str);
        }

        // 点击窗口外部区域可消除
        final View popupWindowView = defaultView.findViewById(R.id.dialog_window);


        defaultView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = new Rect();
                popupWindowView.getGlobalVisibleRect(rect);
                if (!rect.contains(x, y)) {
                    WindowUtils.hidePopupWindow();
                }

                return false;
            }
        });

        // 点击back键可消除
        defaultView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KEYCODE_BACK:
                        WindowUtils.hidePopupWindow();
                        return false;
                    default:
                        return true;
                }
            }
        });
        return defaultView;

    }
}
