package com.zhiyicx.common.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Matrix;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @author LiuChao
 * @describe 可以缩放的View, 和可滑动的列表绑定，摸摸大
 * @date 2017/3/8
 * @contact email:450127106@qq.com
 */

public class ZoomView {
    // 手指移动的最大放缩距离，单位像素
    private static final int MAX_DEFAULT_DISTANCE = 500;
    // 是否正在放大
    private Boolean mScaling = false;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 需要进行放缩的控件
    private View zoomView;
    // 可滑动的列表，当前使用的RecyclerView，你也可以自己添加更多类型的列表
    private RecyclerView mRecyclerView;
    // RecyclerView的布局管理器，用来判断第一个item的位置，如果你把RecyclerView改成其他的ListView，ScrollView，修改判断逻辑
    private LinearLayoutManager mLinearLayoutManager;
    private Activity mActivity;
    // 缩放view的初始宽高,单位像素
    private int originWidth, originHeight;
    // 这个值可以有构造方法传入，先放这儿，留给有缘人
    private int max_distance = MAX_DEFAULT_DISTANCE;

    public ZoomView(View zoomView, Activity activity, RecyclerView recyclerView, int originWidth, int originHeight) {
        this.zoomView = zoomView;
        mActivity = activity;
        mRecyclerView = recyclerView;
        mLinearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        this.originHeight = originHeight;
        this.originWidth = originWidth;
    }

    /**
     * 初始化view的缩放
     */
    public void initZoom() {
        initzoomView();
    }

    /**
     * view缩放的处理
     */
    private void initzoomView() {
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) zoomView.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mScaling = false;
                        replyImage();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        LogUtils.i("zoomView-->" + zoomView.getTop() + " zoomHeight" + zoomView.getHeight()
                                + "  recyclerView-->" + mRecyclerView.getTop()
                                + "postion 0-->" + mLinearLayoutManager.findViewByPosition(0).getTop());
                        if (!mScaling) {
                            //当图片也就是第一个item完全可见的时候，记录触摸屏幕的位置
                            if (mLinearLayoutManager.findViewByPosition(0).getTop() >= zoomView.getTop()) {
                                mFirstPosition = event.getY();
                            } else {
                                break;
                            }
                        }
                        int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                        if (distance < 0 || distance > max_distance) {
                            break;
                        }
                        // 处理放大，需要注意的是，被放缩的控件一定要在父布局的水平方向居中显示，这样放大，才会往两边扩展
                        mScaling = true;
                        lp.width = originWidth + distance;
                        lp.height = originHeight + distance * originHeight / originWidth;
                        zoomView.setLayoutParams(lp);
                        return true; // 返回true表示已经完成触摸事件，不再处理
                }
                return false;
            }
        });

    }

    /**
     * 松开手指，view复原动画
     */
    private void replyImage() {
        final ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) zoomView.getLayoutParams();
        final float w = zoomView.getLayoutParams().width;// 图片当前宽度
        final float h = zoomView.getLayoutParams().height;// 图片当前高度
        final float newW = originWidth;// 图片原宽度
        final float newH = originHeight;// 图片原高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                zoomView.setLayoutParams(lp);
            }
        });
        anim.start();

    }

}
