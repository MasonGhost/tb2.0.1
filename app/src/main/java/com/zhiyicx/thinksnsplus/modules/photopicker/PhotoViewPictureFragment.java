package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.photoview.PhotoViewAttacher;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.utils.TransferImageAnimationUtil;

import java.io.File;

import butterknife.BindView;

/**
 * @author LiuChao
 * @descibe 处理图片放缩动画
 * @date 2017/3/19 0019
 * @contact email:450127106@qq.com
 */
public class PhotoViewPictureFragment extends TSFragment {

    @BindView(R.id.iv_animation)
    ImageView ivAnimation;

    private boolean hasAnim = false;
    private boolean animateIn = false;


    private PhotoViewAttacher mPhotoViewAttacher;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_photoview_picture;
    }

    @Override
    protected void initView(View rootView) {

        mPhotoViewAttacher = new PhotoViewAttacher(ivAnimation);
        mPhotoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().onBackPressed();
            }
        });
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

        final AnimationRectBean rect = getArguments().getParcelable("rect");
        animateIn = getArguments().getBoolean("animationIn");// 是否需要放缩动画，除了第一次进入需要，其他时候应该禁止

        Glide.with(this)
                .load(new File(path))
                .centerCrop()
                .dontAnimate()
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(800, 800)// 这儿很重要，图片太大，会很卡，并且内存溢出
                .thumbnail(0.1f)
                .into(new ImageViewTarget<GlideDrawable>(ivAnimation) {
                    @Override
                    protected void setResource(GlideDrawable glideDrawable) {
                        LogUtils.i(TAG + "setResource");
                        ivAnimation.setImageDrawable(glideDrawable);
                        mPhotoViewAttacher.update();
                        // 获取到模糊图进行放大动画
                        if (!hasAnim && animateIn) {
                            hasAnim = true;
                            startInAnim(rect);
                        }
                    }
                });
    }

    public static PhotoViewPictureFragment newInstance(String path, AnimationRectBean rect,
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
        if (Math.abs(mPhotoViewAttacher.getScale() - 1.0f) > 0.1f) {
            mPhotoViewAttacher.setScale(1, true);
            return;
        }
        getActivity().overridePendingTransition(0, 0);
        animateClose(backgroundAnimator);
    }

    private void animateClose(ObjectAnimator backgroundAnimator) {
        AnimationRectBean rect = getArguments().getParcelable("rect");
        TransferImageAnimationUtil.animateClose(backgroundAnimator, rect, ivAnimation);
    }


    private void startInAnim(final AnimationRectBean rect) {
        final Runnable endAction = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                bundle.putBoolean("animationIn", false);
            }
        };
        TransferImageAnimationUtil.startInAnim(rect, ivAnimation, endAction);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG + "onDestroy");
    }

}
