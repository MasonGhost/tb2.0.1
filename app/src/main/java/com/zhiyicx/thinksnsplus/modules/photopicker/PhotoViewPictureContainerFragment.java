package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author LiuChao
 * @descibe 图片缩放fragment的容器，另外可以处理转圈圈之类的其他操作
 * @date 2017/3/19 0019
 * @contact email:450127106@qq.com
 */
public class PhotoViewPictureContainerFragment extends TSFragment {

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_photoview_picture_container;
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
    protected void initData() {
        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        boolean animateIn = bundle.getBoolean("animationIn");
        bundle.putBoolean("animationIn", false);
        displayPicture(url, animateIn);
    }

    public static PhotoViewPictureContainerFragment newInstance(String url, AnimationRect rect,
                                                boolean animationIn, boolean firstOpenPage) {
        PhotoViewPictureContainerFragment fragment = new PhotoViewPictureContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        bundle.putBoolean("firstOpenPage", firstOpenPage);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void displayPicture(String path, boolean animateIn) {
        // PhotoViewActivity activity = (PhotoViewActivity) getActivity();
        PhotoViewFragment photoViewFragment = (PhotoViewFragment) getParentFragment();
        AnimationRect rect = getArguments().getParcelable("rect");
        boolean firstOpenPage = getArguments().getBoolean("firstOpenPage");
        if (firstOpenPage) {
            if (animateIn) {
                ObjectAnimator animator = photoViewFragment.showBackgroundAnimate();
                animator.start();
            } else {
                photoViewFragment.showBackgroundImmediately();
            }
            getArguments().putBoolean("firstOpenPage", false);
        }

        Fragment fragment = PhotoViewPictureFragment.newInstance(path, rect, animateIn);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_picture_container, fragment)
                .commitAllowingStateLoss();

    }

    public void animationExit(ObjectAnimator backgroundAnimator) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_picture_container);
        PhotoViewPictureFragment child = (PhotoViewPictureFragment) fragment;
        child.animationExit(backgroundAnimator);
    }

    public boolean canAnimateCloseActivity() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_picture_container);
        if (fragment instanceof PhotoViewPictureFragment) {
            return true;
        } else {
            return false;
        }
    }


}
