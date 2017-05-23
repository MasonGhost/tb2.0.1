package com.zhiyicx.thinksnsplus.modules.gallery;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.PathConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressListener;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressModelLoader;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.photoview.PhotoViewAttacher;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.utils.TransferImageAnimationUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
    private int screenW, screenH;

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
        screenW = DeviceUtils.getScreenWidth(context);
        screenH = DeviceUtils.getScreenHeight(context);
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
                        loadOriginImage(mImageBean);
                    }
                });
    }

    @Override
    protected void initData() {
        boolean animateIn = getArguments().getBoolean("animationIn");
        final AnimationRectBean rect = getArguments().getParcelable("rect");
        mImageBean = getArguments() != null ? (ImageBean) getArguments().getParcelable("url") : null;
        assert mImageBean != null;
        if (mImageBean.getImgUrl() != null) {
            // 本地图片不需要查看原图
            mTvOriginPhoto.setVisibility(View.GONE);
            // 本地图片不需要保存
            mPhotoViewAttacherOrigin.setOnLongClickListener(null);
            mPhotoViewAttacherNormal.setOnLongClickListener(null);
        }
        // 显示图片
        if (mImageBean == null) {
            mIvPager.setImageResource(R.drawable.shape_default_image);
        } else {
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);
            if (canLoadImage) {
                loadImage(mImageBean, rect, animateIn);
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
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
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
                        public void onItemClicked() {
                            mActionPopupWindow.hide();
                            saveImage();
                        }
                    })
                    .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                        @Override
                        public void onItemClicked() {
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

    private static final android.view.animation.Interpolator INTERPOLATOR =
            new FastOutSlowInInterpolator();

    /**
     * 显示或者隐藏查看原图按钮
     *
     * @param isIn 是否是进入页面
     */
    public void showOrHideOriginBtn(boolean isIn) {
        // 如果查看原图按钮不可见也就没有必要控制显示隐藏
        if (mTvOriginPhoto.getVisibility() == View.VISIBLE) {
            if (isIn) {
                ViewCompat.animate(mTvOriginPhoto).alpha(1.0f).scaleX(1.0f).scaleY(1.0f)
                        .setDuration(500)
                        .setInterpolator(INTERPOLATOR).withLayer()
                        .start();
            } else {
                ViewCompat.animate(mTvOriginPhoto).alpha(0.0f).scaleX(0.0f).scaleY(0.0f)
                        .setDuration(100)
                        .setInterpolator(INTERPOLATOR).withLayer()
                        .start();
            }
        }
    }

    public void saveImage() {
        // 通过GLide获取bitmap,有缓存读缓存
        Glide.with(getActivity())
                .load(String.format(ApiConfig.IMAGE_PATH.toLowerCase(), mImageBean.getStorage_id(), ImageZipConfig.IMAGE_100_ZIP))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        getSaveBitmapResultObservable(resource);
                    }
                });
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
    private void loadImage(final ImageBean imageBean, final AnimationRectBean rect, final boolean animationIn) {
        LogUtils.e("imageBean = " + imageBean.toString()+"------"+animationIn);

        if (imageBean.getImgUrl() != null) {
            int with = 800;// 图片宽度显示的像素：防止图片过大卡顿
            int height = (int) (with * imageBean.getHeight() / imageBean.getWidth());
            // 加载本地图片
            Glide.with(context)
                    .load(imageBean.getImgUrl())
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .thumbnail(0.1f)
                    .override(with, height)
                    .centerCrop()
                    .into(new GallarySimpleTarget(rect));
        } else {
            // 加载网络图片
            DrawableRequestBuilder thumbnailBuilder = Glide
                    .with(context)
                    .load(new CustomImageSizeModelImp(imageBean)
                            .requestCustomSizeUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            // 尝试从缓存获取原图
            Glide.with(context)
                    .using(cacheOnlyStreamLoader)// 不从网络读取原图
                    .load(String.format(ApiConfig.IMAGE_PATH.toLowerCase(), mImageBean.getStorage_id(), ImageZipConfig.IMAGE_100_ZIP))
                    .thumbnail(thumbnailBuilder)// 加载缩略图，上一个页面已经缓存好了，直接读取
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            LogUtils.i(TAG + "加载原图失败");
                            // 如果不是点击放大进入的那张图片，就需要设置查看原图按钮为缩小状态，这样第一次切换到该页面，才能有放大到1.0的效果
                            if (mTvOriginPhoto != null) {
                                if (!animationIn) {
                                    mTvOriginPhoto.setScaleY(0.0f);
                                    mTvOriginPhoto.setScaleX(0.0f);
                                    mTvOriginPhoto.setAlpha(0.0f);
                                }
                                mTvOriginPhoto.setVisibility(View.VISIBLE);
                            }
                            // 原图没有缓存，从cacheOnlyStreamLoader抛出异常，在这儿加载高清图
                            Glide.with(context)
                                    .using(new CustomImageModelLoader(context))
                                    .load(new CustomImageSizeModelImp(imageBean))
                                    .override(imageBean.getWidth() > screenW ? screenW : (int) imageBean.getWidth(),
                                            imageBean.getHeight() > screenH ? screenH : (int) imageBean.getHeight())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.shape_default_image)
                                    .error(R.drawable.shape_default_image)
//                                    .centerCrop()
                                    .into(new SimpleTarget<GlideDrawable>() {
                                        @Override
                                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                            LogUtils.i(TAG + "加载高清图成功");
                                            if (mIvPager != null) {
                                                mIvPager.setImageDrawable(resource);
                                            }
                                            mPhotoViewAttacherNormal.update();
                                        }
                                    });
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            // 只有获取load的图片才会走这儿，缩略图不会
                            LogUtils.i(TAG + "加载原图成功");
                            mTvOriginPhoto.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .centerCrop()
                    .into(new GallarySimpleTarget(rect));


        }
    }

    // 加载原图:
    private void loadOriginImage(ImageBean imageBean) {
        // 禁止点击查看原图按钮
        mTvOriginPhoto.setClickable(false);
        // 刚点击查看原图，可能会有一段时间，进行重定位请求，所以立即设置进度
        mTvOriginPhoto.setText("0%");
        Glide.with(context)
                .using(new ProgressModelLoader(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 这部分的图片，都是通过 OKHttp 从网络获取的，如果改图片从 glide缓 存中读取，不会经过这儿
                        if (msg.what == ProgressListener.SEND_LOAD_PROGRESS && mTvOriginPhoto != null) {
                            int totalReadBytes = msg.arg1;
                            int lengthBytes = msg.arg2;
                            int progressResult = (int) (((float) totalReadBytes / (float) lengthBytes) * 100);
                            mTvOriginPhoto.setText(progressResult + "%");
                            LogUtils.i("progress-result:-->" + progressResult + " msg.arg1-->" + msg.arg1 + "  msg.arg2-->" +
                                    msg.arg2 + " 比例-->" + progressResult + "%/" + "100%");
                            if (progressResult == 100) {
                                mTvOriginPhoto.setText(R.string.completed);
                            }
                        }
                    }
                }))
                .load(String.format(ApiConfig.IMAGE_PATH.toLowerCase(), imageBean.getStorage_id(), ImageZipConfig.IMAGE_100_ZIP))
//                .override(imageBean.getWidth() > screenW ? screenW : (int) imageBean.getWidth(),
//                        imageBean.getHeight() > screenH ? screenH : (int) imageBean.getHeight())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        LogUtils.i("loadOriginImage  onException");
                        // 如果通过okhttp查看原图，失败，在这儿接收异常
                        mTvOriginPhoto.setText(getString(R.string.see_origin_photos_failure));
                        // 查看失败可以再次点击
                        mTvOriginPhoto.setClickable(true);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })

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
            // 退出隐藏查看原图按钮，防止显示在透明背景上
            mTvOriginPhoto.setVisibility(View.GONE);
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
            // 退出隐藏查看原图按钮，防止显示在透明背景上
            mTvOriginPhoto.setVisibility(View.GONE);
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
                LogUtils.i("startInAnim" + "endAction");
            }
        };
        TransferImageAnimationUtil.startInAnim(rect, mIvPager, endAction);
    }

    /**
     * 通过Rxjava在io线程中处理保存图片的逻辑，得到返回结果，否则会阻塞ui
     */
    private void getSaveBitmapResultObservable(final Bitmap bitmap) {
        Observable.just(1)// 不能empty否则map无法进行转换
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        String imgName = System.currentTimeMillis() + ".jpg";
                        String imgPath = PathConfig.PHOTO_SAVA_PATH;
                        return DrawableProvider.saveBitmap(bitmap, imgName, imgPath);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        switch (result) {
                            case "-1":
                                result = getString(R.string.save_failure1);
                                break;
                            case "-2":
                                result = getString(R.string.save_failure2);
                                break;
                            default:
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                        Uri.parse("file://" + result)));// 更新系统相册
                                result = getString(R.string.save_success) + result;

                        }
                        TSnackbar.make(mSnackRootView, result, TSnackbar.LENGTH_SHORT)
                                .setPromptThemBackground(Prompt.SUCCESS)
                                .setMinHeight(0, getResources().getDimensionPixelSize(R.dimen.toolbar_height))
                                .show();
                    }
                });
    }

    private class GallarySimpleTarget extends SimpleTarget<GlideDrawable> {
        private AnimationRectBean rect;

        public GallarySimpleTarget(AnimationRectBean rect) {
            super();
            this.rect = rect;
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            if (resource == null) {
                return;
            }
            if (mPbProgress != null) {
                mPbProgress.setVisibility(View.GONE);
            }
            if (mIvPager != null) {
                mIvPager.setImageDrawable(resource);
            }
            mPhotoViewAttacherNormal.update();
            // 获取到模糊图进行放大动画
            if (!hasAnim) {
                hasAnim = true;
                startInAnim(rect);
            }
        }

    }

    private static final StreamModelLoader<String> cacheOnlyStreamLoader = new StreamModelLoader<String>() {
        @Override
        public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
            return new DataFetcher<InputStream>() {
                @Override
                public InputStream loadData(Priority priority) throws Exception {
                    // 如果是从网络获取图片肯定会走这儿，直接抛出异常，缓存从其他方法获取
                    throw new IOException("intercupt net by own");
                }

                @Override
                public void cleanup() {

                }

                @Override
                public String getId() {
                    return model;
                }

                @Override
                public void cancel() {

                }
            };
        }
    };


    @Override
    public void onDestroy() {
        DeviceUtils.gc();
        super.onDestroy();
    }


}
