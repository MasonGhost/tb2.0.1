package com.zhiyicx.thinksnsplus.modules.gallery;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressListener;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressModelLoader;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.photoview.PhotoViewAttacher;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;


/**
 * @author LiuChao
 * @describe
 * @date 2017/3/20
 * @contact email:450127106@qq.com
 */

public class GalleryPictureFragment extends TSFragment implements View.OnLongClickListener, PhotoViewAttacher.OnPhotoTapListener {
    @BindView(R.id.iv_orin_pager)
    ImageView mIvOriginPager;
    @BindView(R.id.iv_pager)
    ImageView mIvPager;
    @BindView(R.id.pb_progress)
    ProgressBar mPbProgress;
    @BindView(R.id.tv_origin_photo)
    TextView mTvOriginPhoto;

    private PhotoViewAttacher mPhotoViewAttacherOrigin;
    private PhotoViewAttacher mPhotoViewAttacherNormal;
    private ImageBean mImageBean;
    private ActionPopupWindow mActionPopupWindow;
    private Context context;
    private double mScreenWith;
    private double mScreenHeiht;

    private boolean hasAnim = false;
    public static final int ANIMATION_DURATION = 300;

    public static GalleryPictureFragment newInstance(ImageBean imageUrl) {
        final GalleryPictureFragment f = new GalleryPictureFragment();
        final Bundle args = new Bundle();
        args.putParcelable("url", imageUrl);
        f.setArguments(args);
        return f;
    }

    @Override
    protected void initView(View rootView) {
        context = getContext();
        mScreenWith = DeviceUtils.getScreenWidth(context);
        mScreenHeiht = DeviceUtils.getScreenHeight(context);
        mPhotoViewAttacherNormal = new PhotoViewAttacher(mIvPager);
        mPhotoViewAttacherOrigin = new PhotoViewAttacher(mIvOriginPager);
        mPhotoViewAttacherNormal.setOnPhotoTapListener(this);
        mPhotoViewAttacherOrigin.setOnPhotoTapListener(this);
        // 图片长按，保存
        mIvPager.setOnLongClickListener(this);
        mIvOriginPager.setOnLongClickListener(this);
        RxView.clicks(mTvOriginPhoto)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        loadOriginImage(String.format(ApiConfig.IMAGE_PATH, mImageBean.getStorage_id(), 100));
                    }
                });
    }

    @Override
    protected void initData() {

        boolean animateIn = getArguments().getBoolean("animationIn");
        final AnimationRectBean rect = getArguments().getParcelable("rect");
        mImageBean = getArguments() != null ? (ImageBean) getArguments().getParcelable("url") : null;
        if (mImageBean.getImgUrl() != null) { // 本地图片不需要查看原图
            mTvOriginPhoto.setVisibility(View.GONE);
        }
        // 显示图片
        if (mImageBean == null) {
            mIvPager.setImageResource(R.drawable.shape_default_image);
            return;
        } else {
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);
            if (canLoadImage) {
                loadImage(mImageBean, rect);
            }
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.item_gallery_photo;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        if (mActionPopupWindow == null) {
            mActionPopupWindow = ActionPopupWindow.builder()
                    .backgroundAlpha(1.0f)
                    .bottomStr(context.getString(R.string.cancel))
                    .item1Str(context.getString(R.string.save_to_photo))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .with((Activity) context)
                    .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                        @Override
                        public void onItem1Clicked() {
                            mActionPopupWindow.hide();
                        }
                    })
                    .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                        @Override
                        public void onBottomClicked() {
                            mActionPopupWindow.hide();
                        }
                    })
                    .build();
        }
        mActionPopupWindow.show();
        return false;
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        if (context instanceof Activity) {
            if (!((Activity) context).isFinishing()) {
                ((Activity) context).onBackPressed();
            }
        }
    }

    public void saveImage() {
        //BitmapUtils.saveBitmap(getActivity(), mImageView.getDrawingCache(), StringUtils.getImageNameByUrl(mImageBean));
    }

    public static GalleryPictureFragment newInstance(ImageBean imageBean, AnimationRectBean rect,
                                                     boolean animationIn) {
        GalleryPictureFragment fragment = new GalleryPictureFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("url", imageBean);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        fragment.setArguments(bundle);
        return fragment;
    }

    // 加载图片不带监听
    private void loadImage(final ImageBean imageBean, final AnimationRectBean rect) {
        LogUtils.i("imageBean = " + imageBean.toString());

        if (imageBean.getImgUrl() != null) {
            Glide.with(context)
                    .load(imageBean.getImgUrl())
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .into(mIvPager);
            mPbProgress.setVisibility(View.GONE);
            mPhotoViewAttacherNormal.update();
            return;
        }
//        int with = (int) (imageBean.getWidth() > mScreenWith ? mScreenWith : FrameLayout.LayoutParams.MATCH_PARENT);
        int with = (int) (mScreenWith);
        int height = (int) (imageBean.getHeight() > mScreenHeiht ? mScreenHeiht : FrameLayout.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(with, height);
        layoutParams.gravity = Gravity.CENTER;
        mIvPager.setLayoutParams(layoutParams);

        DrawableRequestBuilder thumbnailBuilder = Glide
                .with(context)
                .load(new CustomImageSizeModelImp(imageBean)
                        .requestCustomSizeUrl());
        Glide.with(context)
                .using(new CustomImageModelLoader(context))
                .load(new CustomImageSizeModelImp(imageBean))
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
                .thumbnail(thumbnailBuilder)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mPbProgress.setVisibility(View.GONE);
                        mIvPager.setImageDrawable(resource);
                        mPhotoViewAttacherNormal.update();
                        // 获取到模糊图进行放大动画
                        if (!hasAnim) {
                            hasAnim = true;
                            startInAnim(rect);
                        }
                    }
                });
    }

    // 加载原图:
    private void loadOriginImage(String imageUrl) {
        Glide.with(context)
                .using(new ProgressModelLoader(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == ProgressListener.SEND_LOAD_PROGRESS) {
                            int totalReadBytes = msg.arg1;
                            int lengthBytes = msg.arg2;
                            int progressResult = (int) (((float) totalReadBytes / (float) lengthBytes) * 100);
                            mTvOriginPhoto.setText(progressResult + "%/" + "100%");
                            LogUtils.i("progress-result:-->" + progressResult + " msg.arg1-->" + msg.arg1 + "  msg.arg2-->" +
                                    msg.arg2 + " 比例-->" + progressResult + "%/" + "100%");
                            if (progressResult == 100) {
                                mIvOriginPager.setVisibility(View.VISIBLE);
                                mIvPager.setVisibility(View.GONE);
                            }
                        }
                    }
                }))
                .load(imageUrl)
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        mTvOriginPhoto.setText(getString(R.string.load_error));
////                        mIvOriginPager.setVisibility(View.VISIBLE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        mTvOriginPhoto.setText(100 + "%/" + "100%");
//                        mIvOriginPager.setVisibility(View.VISIBLE);
//                        mIvPager.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mTvOriginPhoto.setText(100 + "%/" + "100%");
                        mIvOriginPager.setImageDrawable(resource);
                        mPhotoViewAttacherOrigin.update();
                        mIvOriginPager.setVisibility(View.VISIBLE);
                        mIvPager.setVisibility(View.GONE);
                    }
                });
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

        AnimationRectBean rect = getArguments().getParcelable("rect");
        // 没有大图退出动画，直接关闭activity
        if (rect == null) {
            mIvPager.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }
        // 小图rect属性
        final Rect startBounds = rect.scaledBitmapRect;
        // 大图rect属性
        final Rect finalBounds = DrawableProvider.getBitmapRectFromImageView(mIvPager);
        // 没有大图退出动画，直接关闭activity
        if (finalBounds == null || startBounds == null) {
            mIvPager.animate().alpha(0);
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
        mIvPager.setPivotY((mIvPager.getHeight() - finalBounds.height()) / 2);
        mIvPager.setPivotX((mIvPager.getWidth() - finalBounds.width()) / 2);
        // 位移+缩小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mIvPager.animate().translationX(deltaLeft).translationY(deltaTop)
                    .scaleY(startScaleFinal)
                    .scaleX(startScaleFinal).setDuration(ANIMATION_DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mIvPager.animate().alpha(0.0f).setDuration(200).withEndAction(
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

        animationSet.playTogether(ObjectAnimator.ofFloat(mIvPager,
                "clipBottom", 0,
                AnimationRectBean.getClipBottom(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(mIvPager,
                "clipRight", 0,
                AnimationRectBean.getClipRight(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(mIvPager,
                "clipTop", 0, AnimationRectBean.getClipTop(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(mIvPager,
                "clipLeft", 0, AnimationRectBean.getClipLeft(rect, finalBounds)));

        animationSet.start();
    }


    private void startInAnim(final AnimationRectBean rect) {
        final Runnable endAction = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                bundle.putBoolean("animationIn", false);
            }
        };
        mIvPager.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {

                        if (rect == null) {
                            mIvPager.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
                            return true;
                        }

                        final Rect startBounds = new Rect(rect.scaledBitmapRect);
                        final Rect finalBounds =
                                DrawableProvider.getBitmapRectFromImageView(mIvPager);

                        if (finalBounds == null) {
                            mIvPager.getViewTreeObserver().removeOnPreDrawListener(this);
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
                        mIvPager.setPivotY(
                                (mIvPager.getHeight() - finalBounds.height()) / 2);
                        mIvPager.setPivotX((mIvPager.getWidth() - finalBounds.width()) / 2);

                        mIvPager.setScaleX(1 / startScale);
                        mIvPager.setScaleY(1 / startScale);

                        mIvPager.setTranslationX(deltaLeft);
                        mIvPager.setTranslationY(deltaTop);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mIvPager.animate().translationY(0).translationX(0)
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

                        animationSet.playTogether(ObjectAnimator.ofFloat(mIvPager,
                                "clipBottom",
                                AnimationRectBean.getClipBottom(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(mIvPager,
                                "clipRight",
                                AnimationRectBean.getClipRight(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(mIvPager,
                                "clipTop", AnimationRectBean.getClipTop(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(mIvPager,
                                "clipLeft", AnimationRectBean.getClipLeft(rect, finalBounds), 0));

                        animationSet.start();

                        mIvPager.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
    }


}
