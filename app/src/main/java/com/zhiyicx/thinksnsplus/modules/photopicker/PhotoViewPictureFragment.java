package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.widget.TouchImageView;

/**
 * @author LiuChao
 * @descibe  处理图片放缩动画
 * @date  2017/3/19 0019
 * @contact email:450127106@qq.com
 */
public class PhotoViewPictureFragment extends TSFragment {

    @BindView(R.id.iv_animation)
    TouchImageView ivAnimation;

    private boolean hasAnim = false;

    public static final int ANIMATION_DURATION = 300;


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_photoview_picture;
    }

    @Override
    protected void initView(View rootView) {


   /*     LongClickListener longClickListener = ((PhotoViewPictureContainerFragment) getParentFragment())
                .getLongClickListener();
        ivAnimation.setOnLongClickListener(longClickListener);*/

    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initData() {

        final String path = getArguments().getString("path");
        boolean animateIn = getArguments().getBoolean("animationIn");
        final AnimationRect rect = getArguments().getParcelable("rect");

        Glide.with(getContext())
                .load(path)
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        ivAnimation.setImageDrawable(resource);

                        // 获取到模糊图进行放大动画
                        if (!hasAnim) {
                            hasAnim = true;
                            startInAnim(rect);
                        }

                    }
                });
    }

    public static PhotoViewPictureFragment newInstance(String path, AnimationRect rect,
                                                     boolean animationIn) {
        PhotoViewPictureFragment fragment = new PhotoViewPictureFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void animationExit(ObjectAnimator backgroundAnimator) {
        // 图片处于放大状态，先让它复原
       /* if (Math.abs(ivAnimation.getScale() - 1.0f) > 0.1f) {
            ivAnimation.setScale(1, true);
            return;
        }*/

        getActivity().overridePendingTransition(0, 0);
        animateClose(backgroundAnimator);
    }

    private void animateClose(ObjectAnimator backgroundAnimator) {

        AnimationRect rect = getArguments().getParcelable("rect");
        // 没有大图退出动画，直接关闭activity
        if (rect == null) {
            ivAnimation.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }
        // 小图rect属性
        final Rect startBounds = rect.scaledBitmapRect;
        // 大图rect属性
        final Rect finalBounds = DrawableProvider.getBitmapRectFromImageView(ivAnimation);
        // 没有大图退出动画，直接关闭activity
        if (finalBounds == null || startBounds == null) {
            ivAnimation.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // 如果大图的宽度对于高度比小图的宽度对于高度更宽，以高度比来放缩，这样能够避免动画结束，小图边缘出现空白
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }

        final float startScaleFinal = startScale;

        int deltaTop = startBounds.top - finalBounds.top;
        int deltaLeft = startBounds.left - finalBounds.left;
        // 设置XY轴心
        ivAnimation.setPivotY((ivAnimation.getHeight() - finalBounds.height()) / 2);
        ivAnimation.setPivotX((ivAnimation.getWidth() - finalBounds.width()) / 2);
        // 位移+缩小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivAnimation.animate().translationX(deltaLeft).translationY(deltaTop)
                    .scaleY(startScaleFinal)
                    .scaleX(startScaleFinal).setDuration(ANIMATION_DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                ivAnimation.animate().alpha(0.0f).setDuration(200).withEndAction(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        });
                            }
                        }
                    });
        }

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(ANIMATION_DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.playTogether(backgroundAnimator);

        animationSet.playTogether(ObjectAnimator.ofFloat(ivAnimation,
                "clipBottom", 0,
                AnimationRect.getClipBottom(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(ivAnimation,
                "clipRight", 0,
                AnimationRect.getClipRight(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(ivAnimation,
                "clipTop", 0, AnimationRect.getClipTop(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(ivAnimation,
                "clipLeft", 0, AnimationRect.getClipLeft(rect, finalBounds)));

        animationSet.start();
    }


    private void startInAnim(final AnimationRect rect) {
        final Runnable endAction = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                bundle.putBoolean("animationIn", false);
            }
        };
        ivAnimation.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {

                        if (rect == null) {
                            ivAnimation.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
                            return true;
                        }

                        final Rect startBounds = new Rect(rect.scaledBitmapRect);
                        final Rect finalBounds =
                                DrawableProvider.getBitmapRectFromImageView(ivAnimation);

                        if (finalBounds == null) {
                            ivAnimation.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
                            return true;
                        }

                        float startScale = (float) finalBounds.width() / startBounds.width();

                        if (startScale * startBounds.height() > finalBounds.height()) {
                            startScale = (float) finalBounds.height() / startBounds.height();
                        }

                        int deltaTop = startBounds.top - finalBounds.top;
                        int deltaLeft = startBounds.left - finalBounds.left;
                        // 位移+缩小
                        ivAnimation.setPivotY(
                                (ivAnimation.getHeight() - finalBounds.height()) / 2);
                        ivAnimation.setPivotX((ivAnimation.getWidth() - finalBounds.width()) / 2);

                        ivAnimation.setScaleX(1 / startScale);
                        ivAnimation.setScaleY(1 / startScale);

                        ivAnimation.setTranslationX(deltaLeft);
                        ivAnimation.setTranslationY(deltaTop);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ivAnimation.animate().translationY(0).translationX(0)
                                    .scaleY(1)
                                    .scaleX(1).setDuration(ANIMATION_DURATION)
                                    .setInterpolator(
                                            new AccelerateDecelerateInterpolator())
                                    .withEndAction(endAction);
                        }

                        AnimatorSet animationSet = new AnimatorSet();
                        animationSet.setDuration(ANIMATION_DURATION);
                        animationSet
                                .setInterpolator(new AccelerateDecelerateInterpolator());

                        animationSet.playTogether(ObjectAnimator.ofFloat(ivAnimation,
                                "clipBottom",
                                AnimationRect.getClipBottom(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(ivAnimation,
                                "clipRight",
                                AnimationRect.getClipRight(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(ivAnimation,
                                "clipTop", AnimationRect.getClipTop(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(ivAnimation,
                                "clipLeft", AnimationRect.getClipLeft(rect, finalBounds), 0));

                        animationSet.start();

                        ivAnimation.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
    }


    @OnClick(R.id.iv_animation)
    public void onClick() {
        getActivity().onBackPressed();
    }
}
