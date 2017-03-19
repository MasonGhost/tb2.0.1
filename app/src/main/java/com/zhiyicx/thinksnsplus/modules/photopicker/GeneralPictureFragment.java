package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.gallery.CustomImageModelLoader;
import com.zhiyicx.thinksnsplus.modules.gallery.CustomImageSizeModelImp;

import java.io.File;

/**
 * User: qii
 * Date: 14-4-30
 */
public class GeneralPictureFragment extends Fragment {

    private ImageView photoView;
    private boolean hasAnim = false;

    public static final int ANIMATION_DURATION = 300;

    public static GeneralPictureFragment newInstance(String path, AnimationRect rect,
                                                     boolean animationIn) {
        GeneralPictureFragment fragment = new GeneralPictureFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_general_layout, container, false);

        photoView = (ImageView) view.findViewById(R.id.animation);

        if (true) {

            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }

   /*     LongClickListener longClickListener = ((ContainerFragment) getParentFragment())
                .getLongClickListener();
        photoView.setOnLongClickListener(longClickListener);*/

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
                        photoView.setImageDrawable(resource);

                        // 获取到模糊图进行放大动画
                        if (!hasAnim) {
                            hasAnim = true;
                            startInAnim(rect);
                        }

                    }
                });


        return view;
    }

    public void animationExit(ObjectAnimator backgroundAnimator) {
        // 图片处于放大状态，先让它复原
       /* if (Math.abs(photoView.getScale() - 1.0f) > 0.1f) {
            photoView.setScale(1, true);
            return;
        }*/

        getActivity().overridePendingTransition(0, 0);
        animateClose(backgroundAnimator);
    }

    private void animateClose(ObjectAnimator backgroundAnimator) {

        AnimationRect rect = getArguments().getParcelable("rect");
        // 没有大图退出动画，直接关闭activity
        if (rect == null) {
            photoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }
        // 小图rect属性
        final Rect startBounds = rect.scaledBitmapRect;
        // 大图rect属性
        final Rect finalBounds = DrawableProvider.getBitmapRectFromImageView(photoView);
        // 没有大图退出动画，直接关闭activity
        if (finalBounds == null || startBounds == null) {
            photoView.animate().alpha(0);
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
        photoView.setPivotY((photoView.getHeight() - finalBounds.height()) / 2);
        photoView.setPivotX((photoView.getWidth() - finalBounds.width()) / 2);
        // 位移+缩小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            photoView.animate().translationX(deltaLeft).translationY(deltaTop)
                    .scaleY(startScaleFinal)
                    .scaleX(startScaleFinal).setDuration(ANIMATION_DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                photoView.animate().alpha(0.0f).setDuration(200).withEndAction(
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

        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipBottom", 0,
                AnimationRect.getClipBottom(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipRight", 0,
                AnimationRect.getClipRight(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipTop", 0, AnimationRect.getClipTop(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
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
        photoView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {

                        if (rect == null) {
                            photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
                            return true;
                        }

                        final Rect startBounds = new Rect(rect.scaledBitmapRect);
                        final Rect finalBounds =
                                DrawableProvider.getBitmapRectFromImageView(photoView);

                        if (finalBounds == null) {
                            photoView.getViewTreeObserver().removeOnPreDrawListener(this);
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
                        photoView.setPivotY(
                                (photoView.getHeight() - finalBounds.height()) / 2);
                        photoView.setPivotX((photoView.getWidth() - finalBounds.width()) / 2);

                        photoView.setScaleX(1 / startScale);
                        photoView.setScaleY(1 / startScale);

                        photoView.setTranslationX(deltaLeft);
                        photoView.setTranslationY(deltaTop);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            photoView.animate().translationY(0).translationX(0)
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

                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipBottom",
                                AnimationRect.getClipBottom(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipRight",
                                AnimationRect.getClipRight(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipTop", AnimationRect.getClipTop(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipLeft", AnimationRect.getClipLeft(rect, finalBounds), 0));

                        animationSet.start();

                        photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
    }
}
