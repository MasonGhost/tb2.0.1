package com.zhiyicx.thinksnsplus.modules.gallery;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
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
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.jakewharton.rxbinding.view.RxView;
import com.umeng.socialize.utils.BitmapUtils;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.PathConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressListener;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressModelLoader;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.photoview.PhotoViewAttacher;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.utils.TransferImageAnimationUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        mPhotoViewAttacherOrigin.setOnLongClickListener(this);
        mPhotoViewAttacherNormal.setOnLongClickListener(this);

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
                            saveImage();
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

        if (mIvOriginPager.getDrawingCache() != null) {
            // 如果已经加载过原图，那么就从原图的ImageView中直接拿取bitmap
            getSaveBitmapResultObservable(mIvOriginPager.getDrawingCache())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String result) {
                            ToastUtils.showToast(result);
                        }
                    });
        } else {
            // 否则通过GLide获取bitmap
            Glide.with(getActivity())
                    .load(String.format(ApiConfig.IMAGE_PATH, mImageBean.getStorage_id(), 100))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            getSaveBitmapResultObservable(resource)
                                    .subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String result) {
                                            ToastUtils.showToast(result);
                                        }
                                    });
                        }
                    });
        }
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
        // 刚点击查看原图，可能会有一段时间，进行重定位请求，所以立即设置进度
        mTvOriginPhoto.setText("0%/" + "100%");
        Glide.with(context)
                .using(new ProgressModelLoader(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 这部分的图片，都是通过OKHttp从网络获取的，如果改图片从glide缓存中读取，不会经过这儿
                        if (msg.what == ProgressListener.SEND_LOAD_PROGRESS) {
                            int totalReadBytes = msg.arg1;
                            int lengthBytes = msg.arg2;
                            int progressResult = (int) (((float) totalReadBytes / (float) lengthBytes) * 100);
                            mTvOriginPhoto.setText(progressResult + "%/" + "100%");
                            LogUtils.i("progress-result:-->" + progressResult + " msg.arg1-->" + msg.arg1 + "  msg.arg2-->" +
                                    msg.arg2 + " 比例-->" + progressResult + "%/" + "100%");
                            if (progressResult == 100) {
                                mTvOriginPhoto.setText(R.string.completed);
                            }
                        }
                    }
                }))
                .load(imageUrl)
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
                .into(new SimpleTarget<GlideDrawable>() {
                          @Override
                          public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                              mTvOriginPhoto.setText(R.string.completed);
                              mIvOriginPager.setImageDrawable(resource);
                              mPhotoViewAttacherOrigin.update();
                              mIvOriginPager.setVisibility(View.VISIBLE);
                              // 直接隐藏掉图片会有闪烁的效果，通过判断图片渲染成功后，隐藏，平滑过渡
                              Runnable runnable = new Runnable() {
                                  @Override
                                  public void run() {
                                      while (mIvOriginPager.getDrawable() != null) {
                                          mIvPager.setVisibility(View.GONE);
                                          mTvOriginPhoto.setVisibility(View.GONE);
                                          break;
                                      }
                                  }
                              };
                              runnable.run();

                          }
                      }
                );
    }

    /**
     * 退出动画，在返回操作中调用
     *
     * @param backgroundAnimator
     */
    public void animationExit(ObjectAnimator backgroundAnimator) {
        // 高清图片可见，那就高清图片退出动画
        if (mIvPager.getVisibility() == View.VISIBLE) {
            // 图片处于放大状态，先让它复原
            if (Math.abs(mPhotoViewAttacherNormal.getScale() - 1.0f) > 0.1f) {
                mPhotoViewAttacherNormal.setScale(1, true);
                return;
            }
            getActivity().overridePendingTransition(0, 0);
            AnimationRectBean rect = getArguments().getParcelable("rect");
            TransferImageAnimationUtil.animateClose(backgroundAnimator, rect, mIvPager);
        }
        // 原图可见，退出就是用原图
        if (mIvOriginPager.getVisibility() == View.VISIBLE) {
            // 图片处于放大状态，先让它复原
            if (Math.abs(mPhotoViewAttacherOrigin.getScale() - 1.0f) > 0.1f) {
                mPhotoViewAttacherOrigin.setScale(1, true);
                return;
            }
            getActivity().overridePendingTransition(0, 0);
            AnimationRectBean rect = getArguments().getParcelable("rect");
            TransferImageAnimationUtil.animateClose(backgroundAnimator, rect, mIvOriginPager);
        }

    }

    /**
     * 进入动画，在加载图片后调用
     *
     * @param rect
     */
    private void startInAnim(final AnimationRectBean rect) {
        final Runnable endAction = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                bundle.putBoolean("animationIn", false);
            }
        };
        TransferImageAnimationUtil.startInAnim(rect, mIvPager, endAction);
    }

    /**
     * 通过Rxjava在io线程中处理保存图片的逻辑，得到返回结果，否则会阻塞ui
     */
    private Observable<String> getSaveBitmapResultObservable(final Bitmap bitmap) {
        return Observable.just(1)// 不能empty否则map无法进行转换
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        String imgName = System.currentTimeMillis() + ".jpg";
                        String imgPath = PathConfig.PHOTO_SAVA_PATH;
                        return DrawableProvider.saveBitmap(bitmap, imgName, imgPath);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
