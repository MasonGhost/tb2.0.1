package com.zhiyicx.thinksnsplus.modules.music_fm.music_helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity;


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
    private static WindowManager.LayoutParams mLayoutParams;

    private static OnWindowDismisslistener windowDismisslistener;

    private static ImageView mImageView;
    private static RotateAnimation mRotateAnimation;

    public interface OnWindowDismisslistener {
        void onDismiss();
    }

    public static void setWindowDismisslistener(OnWindowDismisslistener windowDismisslistener) {
        WindowUtils.windowDismisslistener = windowDismisslistener;
    }

    public static void showPopupWindow(final Context context) {
        if (isShown) {
            LogUtils.d(LOG_TAG, "return cause already shown");
            return;
        }

        isShown = true;
        // 获取应用的Context
        mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mView = setUpView(context, "");
        mImageView = (ImageView) mView.findViewById(R.id.musci);
        mLayoutParams = new LayoutParams();
        String packname = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android" +
                ".permission.SYSTEM_ALERT_WINDOW", packname));
        if (permission) {
            mLayoutParams.type = LayoutParams.TYPE_PHONE;
        } else {
            mLayoutParams.type = LayoutParams.TYPE_TOAST;
        }

        mLayoutParams.format = PixelFormat.TRANSPARENT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
        mLayoutParams.width = 70;
        mLayoutParams.height = 70;
        mLayoutParams.x = 20;
        mLayoutParams.y = 5;

        mRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim
                .music_window_rotate);
        mImageView.setAnimation(mRotateAnimation);
        mRotateAnimation.start();
        mWindowManager.addView(mView, mLayoutParams);

    }

    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
        if (isShown && null != mView) {
            if (windowDismisslistener != null) {
                windowDismisslistener.onDismiss();
            }
            mWindowManager.removeView(mView);
            isShown = false;
        }

    }

    private static View setUpView(final Context context, String str) {
        View defaultView = LayoutInflater.from(context).inflate(R.layout.windows_music,
                null);

        defaultView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(context, MusicPlayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent
                            .FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                }
                return true;
            }
        });
        return defaultView;
    }

    public static void goLeft() {
        mLayoutParams.x = 90;
        mLayoutParams.y = 5;
        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }

    public static void goRight() {
        mLayoutParams.x = 20;
        mLayoutParams.y = 5;
        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }
}
