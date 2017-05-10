package com.zhiyicx.thinksnsplus.widget.coordinatorlayout;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zhiyicx.baseproject.widget.refresh.CoodinatorLayoutAndRecyclerViewRefreshLayout;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author LiuChao
 * @describe view跟随列表滑动，显示和隐藏：类似floatingbutton的效果
 * @date 2017/4/14
 * @contact email:450127106@qq.com
 */

public class ScrollAwareFABBehavior extends CoordinatorLayout.Behavior {

    private static final android.view.animation.Interpolator INTERPOLATOR =
            new FastOutSlowInInterpolator();
    private boolean mIsAnimatingOut = false;

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        // 确定滑动方向为垂直方向
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);
        // 控件可见时才有动画，没有时不要动
        if (dyConsumed > 0 && !this.mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
            // 向上滑动
            animateOut(child);
        } else if (dyConsumed < 0 && child.getVisibility() == View.VISIBLE) {
            animateIn(child);
        }
    }

    // 退出动画
    private void animateOut(final View button) {
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(button).translationY(100.0f).alpha(0.0f).scaleX(0.0f).scaleY(0.0f)
                    .setInterpolator(INTERPOLATOR).withLayer()
                    .setListener(new ViewPropertyAnimatorListener() {
                        public void onAnimationStart(View view) {
                            ScrollAwareFABBehavior.this.mIsAnimatingOut = true;
                        }

                        public void onAnimationCancel(View view) {
                            ScrollAwareFABBehavior.this.mIsAnimatingOut = false;
                        }

                        public void onAnimationEnd(View view) {
                            ScrollAwareFABBehavior.this.mIsAnimatingOut = false;
                        }
                    }).start();
        }
    }

    // 进入动画
    private void animateIn(View button) {
        if (Build.VERSION.SDK_INT >= 14) {
            // 这里的translationY表示的是移动到起始点
            ViewCompat.animate(button).translationY(0.0F).alpha(1.0F).scaleX(1.0F).scaleY(1.0F)
                    .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                    .start();

        }
    }
}

