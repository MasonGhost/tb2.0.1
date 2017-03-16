package com.zhiyicx.thinksnsplus.modules.music_fm.music_helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity;

public class MusicWindows {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private static View sAddView;
    private ImageView mImageView;
    private RotateAnimation mRotateAnimation;
    private Context mContext;

    public MusicWindows(final Context context) {
        mContext = context;
        getWindowManager(context);
        sAddView = LayoutInflater.from(context).inflate(R.layout.windows_music,
                null);
        mImageView = (ImageView) sAddView.findViewById(R.id.musci);
        sAddView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(context, MusicPlayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent
                            .FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                return true;
            }
        });
    }


    private void getWindowManager(Context context) {
        mWindowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.TRANSPARENT;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;

        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
        mLayoutParams.width = 70;
        mLayoutParams.height = 70;
        mLayoutParams.x = 20;
        mLayoutParams.y = 5;
    }

    public void goLeft() {
        mLayoutParams.x = 90;
        mLayoutParams.y = 5;
        mWindowManager.updateViewLayout(sAddView, mLayoutParams);
    }

    public void goRight() {
        mLayoutParams.x = 20;
        mLayoutParams.y = 5;
        mWindowManager.updateViewLayout(sAddView, mLayoutParams);
    }

    public void showWindows() {
        mRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim
                .music_window_rotate);
        mImageView.setAnimation(mRotateAnimation);
        mRotateAnimation.start();
        mWindowManager.addView(sAddView, mLayoutParams);
    }

    public void hideWindows() {
        try {
            mWindowManager.removeViewImmediate(sAddView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}