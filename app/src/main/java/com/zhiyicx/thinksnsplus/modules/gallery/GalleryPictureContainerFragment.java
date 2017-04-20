package com.zhiyicx.thinksnsplus.modules.gallery;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

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
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        ImageBean imageBean = bundle.getParcelable("url");
        boolean animateIn = bundle.getBoolean("animationIn");
        bundle.putBoolean("animationIn", false);
        displayPicture(imageBean, animateIn);
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

        Fragment fragment = GalleryPictureFragment.newInstance(imageBean, rect, animateIn);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_picture_container, fragment)
                .commitAllowingStateLoss();

    }

    public GalleryPictureFragment getChildFragment() {
        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
        if (fragmentList != null && !fragmentList.isEmpty()) {
            Fragment fragment = fragmentList.get(0);
            if (fragment instanceof GalleryPictureFragment) {
                GalleryPictureFragment galleryPictureFragment = (GalleryPictureFragment) fragment;
                return galleryPictureFragment;
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
        if (fragment instanceof GalleryPictureFragment) {
            return true;
        } else {
            return false;
        }
    }
}
