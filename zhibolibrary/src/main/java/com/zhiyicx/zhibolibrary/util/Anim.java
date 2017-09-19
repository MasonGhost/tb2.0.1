package com.zhiyicx.zhibolibrary.util;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhy.autolayout.AutoLinearLayout;


/**
 * activity切换的动画工具类
 */
public class Anim {
    public static void exit(Activity obj) {
        obj.overridePendingTransition(R.anim.vote_slide_in_from_left, R.anim.slide_out_from_right);
    }

    public static void in(Activity obj) {
        obj.overridePendingTransition(R.anim.slide_in_from_right, R.anim.vote_slide_out_from_left);
    }


    // 刷新动画
    public static void refresh(Context context, View v, int id) {
        v.setBackgroundDrawable(context.getResources().getDrawable(id));
        Animation anim = AnimationUtils.loadAnimation(context,
                R.anim.title_progress);
        // 动画一直不断保持变化
        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);
        v.startAnimation(anim);
    }

    public static void cleanAnim(ImageView animView) {
        if (animView == null)
            return;
        animView.setImageResource(0);
        animView.clearAnimation();
        animView.setVisibility(View.GONE);
    }

    /**
     * 从下往上退出当前，显示下一个
     * 显示效果为下一个activity从底部推出当前activity
     */
    public static void startActivityFromBottom(Activity obj) {
        obj.overridePendingTransition(R.anim.activity_upword_in,
                R.anim.activity_upoword_out);
    }

    /**
     * 从上往下退出
     * 显示效果为下一个activity从顶部推出当前activity
     *
     * @param obj
     */
    public static void startActivityFromTop(Activity obj) {
        obj.overridePendingTransition(R.anim.vote_slide_in_from_bottom,
                R.anim.vote_slilde_out_from_bottom);
    }

    /**
     * 执行礼物动画
     *
     * @param view
     */
    public static void showGiftAnimate(final AutoLinearLayout view, final TextView voteKey, Context context) {

        AnimatorSet animSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.anim_gift);
        animSet.setTarget(view);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                if (voteKey != null) {
                    voteKey.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if (voteKey != null) {
            AnimatorSet animSet1 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.anim_gift);
            animSet1.setTarget(voteKey);
            animSet1.start();
        }

//        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "alpha",
//                0.0f, 1f);
//        anim1.setDuration(800);
//        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view,
//                "alpha", 1.0f, 1.0f);
//        anim2.setDuration(1000);
//        ValueAnimator anim3 = ValueAnimator.ofFloat(0, 1);
//        anim3.reverse();
//        anim3.setDuration(500);
//        anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                view.setTranslationY((Float) animation.getAnimatedValue() * (60));
//                view.setAlpha(Math.abs((Float) animation.getAnimatedValue() - 1));
//            }
//        });
//        /**
//         * anim1，anim2,anim3同时执行
//         * anim4接着执行
//         */
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.play(anim1);
//        animSet.play(anim2).after(anim1);
////		animSet.setDuration(1000);
//        animSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                view.setVisibility(View.INVISIBLE);
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        animSet.start();
    }
}
