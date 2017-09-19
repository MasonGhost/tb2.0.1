package com.zhiyicx.zhibolibrary.ui.components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhiyicx.zhibolibrary.R;

import java.util.Random;

/**
 * Created by jess on 16/5/17.
 */
public class FavorLayout extends RelativeLayout {

    private final String TAG = this.getClass().getSimpleName();
    private Random random = new Random();//用于实现随机功能
    private int dHeight;//爱心的高度
    private int dWidth;//爱心的宽度
    private int mHeight;//FavorLayout的高度
    private int mWidth;//FavorLayout的宽度
    private final static int COLOR_SIZE=6;//多少种颜色
    private final int mMaxCount = 30;//同时最大存在

    public int getSelfColor() {
        return mSelfColor;
    }

    private int mSelfColor = random.nextInt(COLOR_SIZE);//自己点的颜色

    public void setOhterColor(int ohterColor) {
        mOhterColor = ohterColor;
    }

    private int mOhterColor = random.nextInt(COLOR_SIZE);//别人点赞的颜色


    private int mRemainCount;//剩下没有添加到屏幕的心


    //定义一个LayoutParams 用它来控制子view的位置
    private LayoutParams lp;

    //    private Drawable red;
//    private Drawable yellow;
//    private Drawable blue;
//    private Drawable green;
    private int[] drawables;

    //    // 我为了实现 变速效果 挑选了几种插补器
//    private Interpolator line = new LinearInterpolator();//线性
//    private Interpolator acc = new AccelerateInterpolator();//加速
//    private Interpolator dce = new DecelerateInterpolator();//减速
//    private Interpolator accdec = new AccelerateDecelerateInterpolator();//先加速后减速
    // 在init中初始化
//    private Interpolator[] interpolators;
    private Interpolator interpolator;


    public FavorLayout(Context context) {
        this(context, null);
    }

    public FavorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FavorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //重写onMeasure 获取控件宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //注意!! 获取本身的宽高 需要在测量之后才有宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }


    private void init() {
//初始化显示的图片
        drawables = new int[COLOR_SIZE];
//        red = getResources().getDrawable(R.mipmap.heart_red_100);
//        yellow = getResources().getDrawable(R.mipmap.heart_yellow_100);
//        blue = getResources().getDrawable(R.mipmap.heart_blue_100);
//        green = getResources().getDrawable(R.mipmap.heart_green_100);
        drawables[0] = R.mipmap.heart_blue;
        drawables[1] = R.mipmap.heart_green;
        drawables[2] = R.mipmap.heart_orange;
        drawables[3] = R.mipmap.heart_purple;
        drawables[4] = R.mipmap.heart_red;
        drawables[5] = R.mipmap.heart_yellow;
        // 初始化插补器
//        interpolators = new Interpolator[4];
//        interpolators[0] = new LinearInterpolator();
//        interpolators[1] = new AccelerateInterpolator();
//        interpolators[2] = new DecelerateInterpolator();
//        interpolators[3] = new AccelerateDecelerateInterpolator();
        interpolator = new AccelerateDecelerateInterpolator();
        //获取图的宽高 用于后面的计算
        //注意 我这里图片的大小都是一样的,所以我只取了一个
        dHeight = getResources().getDrawable(drawables[0]).getIntrinsicHeight();
        dWidth = getResources().getDrawable(drawables[0]).getIntrinsicWidth();
        //底部 并且 水平居中
        lp = new LayoutParams(dWidth, dHeight);
//        lp.addRule(CENTER_HORIZONTAL, TRUE);//这里的TRUE 要注意 不是true
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

    }

    /**
     * 提供给外部调用,增加爱心
     */
    public void addFavor(boolean isSelf) {
        if (!isSelf && getChildCount() >= mMaxCount) {//如果屏幕上超出最大能容纳的数量则不再添加
            mRemainCount++;
            return;
        }
        ImageView imageView = new ImageView(getContext());

        //随机选一个
        if (isSelf) {
            imageView.setImageResource(drawables[mSelfColor]);//如果是自己点击的固定一个颜色


        }
        else {
            imageView.setImageResource(drawables[mOhterColor]);
        }
        imageView.setLayoutParams(lp);

        addView(imageView);
        Log.v(TAG, "add后子view数:" + getChildCount());
        imageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        Animator set = getAnimator(imageView, isSelf);
        set.addListener(new AnimEndListener(imageView, isSelf));
        set.start();
    }

    /**
     * 动画结束监听
     */
    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;
        private boolean isSelf;

        public AnimEndListener(View target, boolean isSelf) {
            this.target = target;
            this.isSelf = isSelf;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉
            removeView((target));
            target.setLayerType(View.LAYER_TYPE_NONE, null);
            if (!isSelf && mRemainCount > 0) {//动画消失后检查之前是否有没有添加到屏幕的心,继续添加
                mRemainCount--;
                addFavor(false);
            }
            Log.v(TAG, "removeView后子view数:" + getChildCount());
        }
    }


    //最开始的动画没有位移
    //我封装了一个方法 利用ObjectAnimator AnimatorSet来实现 alpha以及x,y轴的缩放功
    //target就是爱心
    private AnimatorSet getEnterAnimtor(final View target) {

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(300);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }

    private ValueAnimator getBezierValueAnimator(View target) {

        //初始化一个BezierEvaluator
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));

        //这里最好画个图 理解一下 传入了起点 和 终点
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF((mWidth - dWidth) / 2, mHeight - dHeight), new PointF(random.nextInt(getWidth()), 0));//随机
        animator.addUpdateListener(new BezierListenr(target));
        animator.setTarget(target);
        animator.setDuration(3000);
        return animator;
    }

    //这里涉及到另外一个方法:getPointF(),这个是我用来获取途径的两个点
    // 这里的取值可以随意调整,调整到你希望的样子就好

    /**
     * 获取中间的两个点
     *
     * @param scale
     */
    private PointF getPointF(int scale) {

        PointF pointF = new PointF();
        pointF.x = random.nextInt((mWidth - 100));//减去100 是为了控制 x轴活动范围,看效果 随意~~
        //再Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些 也可以用其他方法
        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }


    private class BezierListenr implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListenr(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // 这里偷个懒,顺便做一个alpha动画,这样alpha渐变也完成啦
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    private Animator getAnimator(View target, boolean isSelf) {
//        AnimatorSet set = getEnterAnimtor(target);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(bezierValueAnimator);
//        finalSet.playSequentially(set);
//        finalSet.playSequentially(set, bezierValueAnimator);
//        if (isSelf) {
//            finalSet.setInterpolator(interpolators[3]);//如果是自己点击的,使用加速减速插补器
//        }
//        else {
//            finalSet.setInterpolator(interpolators[random.nextInt(4)]);//实现随机变速
//        }
        finalSet.setInterpolator(interpolator);
        finalSet.setTarget(target);
        return finalSet;
    }
}

//我们自定义一个BezierEvaluator 实现 TypeEvaluator
//由于我们view的移动需要控制x y 所以就传入PointF 作为参数;
class BezierEvaluator implements TypeEvaluator<PointF> {


    private PointF pointF1;//途径的两个点
    private PointF pointF2;

    public BezierEvaluator(PointF pointF1, PointF pointF2) {
        this.pointF1 = pointF1;
        this.pointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float time, PointF startValue,
                           PointF endValue) {

        float timeLeft = 1.0f - time;
        PointF point = new PointF();//结果

        PointF point0 = (PointF) startValue;//起点

        PointF point3 = (PointF) endValue;//终点
        //代入公式
        point.x = timeLeft * timeLeft * timeLeft * (point0.x)
                + 3 * timeLeft * timeLeft * time * (pointF1.x)
                + 3 * timeLeft * time * time * (pointF2.x)
                + time * time * time * (point3.x);

        point.y = timeLeft * timeLeft * timeLeft * (point0.y)
                + 3 * timeLeft * timeLeft * time * (pointF1.y)
                + 3 * timeLeft * time * time * (pointF2.y)
                + time * time * time * (point3.y);
        return point;
    }
}