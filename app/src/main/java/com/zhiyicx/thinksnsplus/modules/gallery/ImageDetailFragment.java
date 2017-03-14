package com.zhiyicx.thinksnsplus.modules.gallery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressListener;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressModelLoader;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */

public class ImageDetailFragment extends TSFragment implements View.OnLongClickListener {
    @BindView(R.id.iv_orin_pager)
    ImageView mIvOriginPager;
    @BindView(R.id.iv_pager)
    ImageView mIvPager;
    @BindView(R.id.pb_progress)
    ProgressBar mPbProgress;
    @BindView(R.id.tv_origin_photo)
    TextView mTvOriginPhoto;
    private ImageBean mImageBean;
    private ActionPopupWindow mActionPopupWindow;
    private Context context;
    private double mScreenWith;
    private double mScreenHeiht;

    public static ImageDetailFragment newInstance(ImageBean imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
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
        mImageBean = getArguments() != null ? (ImageBean) getArguments().getParcelable("url") : null;
        if (mImageBean.getImgUrl() != null) { // 本地图片不需要查看原图
            mTvOriginPhoto.setVisibility(View.GONE);
        }

        // 图片长按，保存
        mIvPager.setOnLongClickListener(this);
        mIvOriginPager.setOnLongClickListener(this);
        // 显示图片
        if (mImageBean == null) {
            mIvPager.setImageResource(R.drawable.shape_default_image);
            return;
        } else {
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);
            if (canLoadImage) {
                loadImage(mImageBean, true);
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.item_gallery_photo;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }


    @OnClick({R.id.iv_pager, R.id.tv_origin_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pager:
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing()) {
                        ((Activity) context).onBackPressed();
                    }
                }
                break;
            case R.id.tv_origin_photo:
                loadOriginImage(String.format(ApiConfig.IMAGE_PATH, mImageBean.getStorage_id(), 100));
                break;
        }
    }

    public void saveImage() {
        //BitmapUtils.saveBitmap(getActivity(), mImageView.getDrawingCache(), StringUtils.getImageNameByUrl(mImageBean));
    }
//
//    // 加载较小的缩略图
//    private void loadSmallImage() {
//        loadImage(mImageBean.getImgUrl()==null?mImageBean.getStorage_id() + "/"+mImageBean.getPart():mImageBean.getImgUrl(), true);
//    }
//
//    // 加载较大缩略图
//    private void loadBigImage() {
//        loadImage(mImageBean + "/50", false);
//    }

    // 加载图片不带监听
    private void loadImage(final ImageBean imageBean, final boolean isCycle) {
        LogUtils.i("imageBean = " + imageBean.toString());

        if (imageBean.getImgUrl() != null) {
            Glide.with(context)
                    .load(imageBean.getImgUrl())
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .into(mIvPager);
            mPbProgress.setVisibility(View.GONE);
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
                .listener(new RequestListener<CustomImageSizeModel, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, CustomImageSizeModel model, Target<GlideDrawable> target, boolean isFirstResource) {
                        // 加载本地图片
                        mPbProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, CustomImageSizeModel model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mPbProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .thumbnail(thumbnailBuilder)
                .into(mIvPager);
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
                        mIvOriginPager.setVisibility(View.VISIBLE);
                        mIvPager.setVisibility(View.GONE);
                    }
                });
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
}
