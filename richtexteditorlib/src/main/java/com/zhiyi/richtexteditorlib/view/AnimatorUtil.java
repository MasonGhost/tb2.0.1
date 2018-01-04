package com.zhiyi.richtexteditorlib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

class AnimatorUtil {
    static void show(final BottomMenu bottomMenu, final long duration){

        bottomMenu.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                final ViewGroup.MarginLayoutParams layoutParams= (ViewGroup.MarginLayoutParams) bottomMenu.getLayoutParams();
                bottomMenu.animate()
                        .translationY(0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                bottomMenu.setVisibility(View.VISIBLE);
                                bottomMenu.setAlpha(0);
                            }
                        })
                        .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                layoutParams.setMargins(layoutParams.leftMargin, (int) (-bottomMenu.getTranslationY()),
                                        layoutParams.rightMargin,layoutParams.bottomMargin);
                                bottomMenu.requestLayout();
                            }
                        })
                        .setDuration(duration)
                        .alpha(1f).start();
            }
        });

    }

    static void hide(final BottomMenu bottomMenu, final long duration){
        bottomMenu.post(new Runnable() {
            @Override
            public void run() {
                bottomMenu.animate()
                        .setDuration(duration)
                        .translationY(bottomMenu.getHeight())
                        .alpha(0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                bottomMenu.setVisibility(View.GONE);
                            }
                        }).start();
            }
        });
    }

    static void setTransition(ViewGroup viewGroup,int width,int height){
        //设置给ViewGroup容器
//        viewGroup.setLayoutTransition(new LayoutTransition());
//        ObjectAnimator addAnimator = ObjectAnimator.ofFloat(null, "translationX",height ,0);
//        ObjectAnimator removeAnimator = ObjectAnimator.ofFloat(null, "translationY", 0,- height);
//        viewGroup.getLayoutTransition().setDuration(100);
//        viewGroup.getLayoutTransition()
//                .setAnimator(LayoutTransition.APPEARING, addAnimator);
//        viewGroup.getLayoutTransition()
//                .setAnimator(LayoutTransition.DISAPPEARING, removeAnimator);

    }
}
