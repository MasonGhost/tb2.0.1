package com.zhiyicx.baseproject.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author legendary_tym
 * @Title SpicyCommunity
 * @Package com.zycx.spicycommunity.utils
 * @Description: 音乐播放提示窗口, dialog似乎必须依附于contextwrapper。
 * 待优化、单例&判断view
 * @date: 2016-11-22 11:46
 */
public class WindowUtils {
    private static final String LOG_TAG = "WindowUtils";
    public static final boolean CAN_DRAG = false;
    private static View mView = null;
    private static WindowManager mWindowManager = null;
    private static Bundle sMusicAlbumDetailsBean;

    private static Boolean isShown = false;
    private static Boolean isPause = false;
    private static WindowManager.LayoutParams mLayoutParams;

    private static ImageView mImageView;
    private static RotateAnimation mRotateAnimation;

    private static AnimationTimerTask mAnimationTask;
    private static Timer mAnimationTimer;
    private static Handler mHander = new Handler();
    private static AblumHeadInfo sAblumHeadInfo;

    private static int mWidth;
    private static int mHeight;
    private static float mPrevX;
    private static float mPrevY;
    private static int mAnimatonPeriodTime = 16;
    private static boolean isMove = false;
    private static BigDecimal mStartClickTime;
    private static CopyOnWriteArrayList<OnWindowDismisslistener> sDismisslistenerLists = new CopyOnWriteArrayList<>();

    public interface OnWindowDismisslistener {
        void onDismiss();
    }

    public static void setWindowDismisslistener(OnWindowDismisslistener windowDismisslistener) {
        sDismisslistenerLists.add(windowDismisslistener);
    }

    public static void removeWindowDismisslistener(OnWindowDismisslistener windowDismisslistener) {
        sDismisslistenerLists.remove(windowDismisslistener);
    }

    public static void showPopupWindow(final Context context) {
        if (isShown) {
            LogUtils.d(LOG_TAG, "return cause already shown");
            return;
        }

        isShown = true;
        // 获取WindowManager
        mWindowManager = (WindowManager) context
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
        mLayoutParams.width = ConvertUtils.dp2px(context, 24);
        mLayoutParams.height = ConvertUtils.dp2px(context, 24);
        mLayoutParams.x = ConvertUtils.dp2px(context, 15);
        mLayoutParams.y = ConvertUtils.dp2px(context, 44) / 4;

        mRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim
                .music_window_rotate);
        mImageView.setAnimation(mRotateAnimation);
        mRotateAnimation.start();
        mWindowManager.addView(mView, mLayoutParams);
        initClickAndDrag(context);
    }

    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
        if (isShown && null != mView) {
            for (OnWindowDismisslistener windowDismisslistener : sDismisslistenerLists) {
                if (windowDismisslistener != null) {
                    windowDismisslistener.onDismiss();
                }
            }
            mWindowManager.removeView(mView);
            isShown = false;
            if (mView != null) {
                mView = null;
            }
        }

    }

    private static View setUpView(final Context context, String str) {
        return LayoutInflater.from(context).inflate(R.layout.windows_music,
                null);
    }

    public static Boolean getIsShown() {
        return isShown;
    }

    public static Boolean getIsPause() {
        return isPause;
    }

    public static void setIsPause(Boolean isPause) {
        WindowUtils.isPause = isPause;
    }

    public static void changeToWhiteIcon() {
        if (mImageView != null) {
            mImageView.setImageResource(R.mipmap.music_ico_suspension_white);
        }
    }

    public static void changeToBlackIcon() {
        if (mImageView != null) {
            mImageView.setImageResource(R.mipmap.music_ico_suspension_black);
        }
    }

    public static Bundle getMusicAlbumDetailsBean() {
        return sMusicAlbumDetailsBean;
    }

    public static void setMusicAlbumDetailsBean(Bundle musicAlbumDetailsBean) {
        sMusicAlbumDetailsBean = musicAlbumDetailsBean;
    }

    private static void initClickAndDrag(final Context context) {
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        mPrevX = motionEvent.getRawX();
                        mPrevY = motionEvent.getRawY();
                        mStartClickTime = BigDecimal.valueOf(System.currentTimeMillis());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = motionEvent.getRawX() - mPrevX;
                        float deltaY = motionEvent.getRawY() - mPrevY;
                        mLayoutParams.x -= deltaX;
                        mLayoutParams.y += deltaY;
                        mPrevX = motionEvent.getRawX();
                        mPrevY = motionEvent.getRawY();

//                        if (mLayoutParams.x < 0) mLayoutParams.x = 0;
//                        if (mLayoutParams.x > mWidth - mView.getWidth()) mLayoutParams.x = mWidth - mView.getWidth();
//                        if (mLayoutParams.y < 0) mLayoutParams.y = 0;
//                        if (mLayoutParams.y > mHeight - mView.getHeight() * 2) mLayoutParams.y = mHeight - mView.getHeight() * 2;

                        try {
                            if (CAN_DRAG) {
                                mWindowManager.updateViewLayout(mView, mLayoutParams);
                            }
                        } catch (Exception e) {
                            LogUtils.d(e.toString());
                        }

                        if (deltaX > 10 | deltaY > 10) isMove = true;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:

                        BigDecimal now = BigDecimal.valueOf(System.currentTimeMillis());
                        if (!isMove && (Math.abs(now.subtract(mStartClickTime).floatValue()) < 500)) {
                            // TODO.. click
                            if (getMusicAlbumDetailsBean() == null) {
                                return true;
                            }

                            Intent intent1 = new Intent("android.intent.action.MAIN");
                            intent1.setClassName(context, "com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity");
                            intent1.putExtra("music_info", getMusicAlbumDetailsBean());
                            intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                            return false;
                        }

//                        mAnimationTimer = new Timer();
//                        mAnimationTask = new AnimationTimerTask();
//                        mAnimationTimer.schedule(mAnimationTask, 0, mAnimatonPeriodTime);

                        break;
                }

                return false;
            }
        });
    }

    static class AnimationTimerTask extends TimerTask {
        int mStepX;
        int mDestX;

        public AnimationTimerTask() {
            if (mLayoutParams.x > mWidth / 2) {
                mDestX = mWidth - mView.getWidth();
                mStepX = (mWidth - mLayoutParams.x) / 10;
            } else {
                mDestX = 0;
                mStepX = -((mLayoutParams.x) / 10);
            }

        }

        @Override
        public void run() {
            if (Math.abs(mDestX - mLayoutParams.x) <= Math.abs(mStepX)) {
                mLayoutParams.x = mDestX;
            } else {
                mLayoutParams.x += mStepX;
            }

            try {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        mWindowManager.updateViewLayout(mView, mLayoutParams);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mLayoutParams.x == mDestX) {
                mAnimationTask.cancel();
                mAnimationTimer.cancel();
            }
        }
    }

    public static AblumHeadInfo getAblumHeadInfo() {
        return sAblumHeadInfo;
    }

    public static void setAblumHeadInfo(AblumHeadInfo ablumHeadInfo) {
        sAblumHeadInfo = ablumHeadInfo;
    }

    public static class AblumHeadInfo {
        int listenCount;
        int shareCount;
        int commentCount;
        int likeCount;
        int ablumId;

        public int getAblumId() {
            return ablumId;
        }

        public void setAblumId(int ablumId) {
            this.ablumId = ablumId;
        }

        public int getListenCount() {
            return listenCount;
        }

        public void setListenCount(int listenCount) {
            this.listenCount = listenCount;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }
    }
}
