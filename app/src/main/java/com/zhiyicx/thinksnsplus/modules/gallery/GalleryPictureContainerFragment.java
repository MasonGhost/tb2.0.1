package com.zhiyicx.thinksnsplus.modules.gallery;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/20
 * @contact email:450127106@qq.com
 */

public class GalleryPictureContainerFragment extends TSFragment {
    private boolean mIsLoaded;  // 是否加载过了
    private boolean mIsViewPrep;// view 是否准备好了
    private boolean mIsVisible; // fragment 是否可见
    private boolean mAnimateIn; // 是否是动画进入
    private ImageBean mImageBean; // 图片资源
    private Fragment mContentFragment; // 图片的内容

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_gallary_picture_container_layout;
    }

    @Override
    protected void initView(View rootView) {

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisible = true;
            handleData();
        } else {
            mIsVisible = false;
        }
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
    protected void initData() {
        Bundle bundle = getArguments();
        mImageBean = bundle.getParcelable("url");
        mAnimateIn = bundle.getBoolean("animationIn");
        mIsViewPrep = true;
        handleData();
    }

    public void preLoadData() {
        if (!mIsLoaded && mImageBean != null) {
            displayPicture(mImageBean, mAnimateIn);
        }
    }

    private void handleData() {
        if (mIsViewPrep && mIsVisible) {
            displayPicture(mImageBean, mAnimateIn);
        }
    }

    public static GalleryPictureContainerFragment newInstance(ImageBean imageBean, AnimationRectBean rect,
                                                              boolean animationIn, boolean firstOpenPage) {
        GalleryPictureContainerFragment fragment = new GalleryPictureContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("url", imageBean);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        bundle.putBoolean("firstOpenPage", firstOpenPage);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void displayPicture(ImageBean imageBean, boolean animateIn) {
        mIsLoaded = true;
        GalleryFragment galleryFragment = (GalleryFragment) getParentFragment();
        AnimationRectBean rect = getArguments().getParcelable("rect");
        boolean firstOpenPage = getArguments().getBoolean("firstOpenPage");
        if (firstOpenPage) {
            if (animateIn) {
                ObjectAnimator animator = galleryFragment.showBackgroundAnimate();
                animator.start();
            } else {
                galleryFragment.showBackgroundImmediately();
            }
            getArguments().putBoolean("firstOpenPage", false);
        }
        if (mContentFragment == null) {
            mContentFragment = GalleryPictureFragment.newInstance(imageBean, rect, animateIn);
            getChildFragmentManager().beginTransaction().replace(R.id.fl_picture_container, mContentFragment)
                    .commitAllowingStateLoss();
        }
        this.mAnimateIn = false;

    }

    public GalleryPictureFragment getChildFragment() {
        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
        if (fragmentList != null && !fragmentList.isEmpty()) {
            Fragment fragment = fragmentList.get(0);
            if (fragment instanceof GalleryPictureFragment) {
                return (GalleryPictureFragment) fragment;
            }
        }
        return null;
    }

    public void animationExit(ObjectAnimator backgroundAnimator) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_picture_container);
        GalleryPictureFragment child = (GalleryPictureFragment) fragment;
        child.animationExit(backgroundAnimator);
    }

    public boolean canAnimateCloseActivity() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_picture_container);
        return fragment instanceof GalleryPictureFragment;
    }
}
