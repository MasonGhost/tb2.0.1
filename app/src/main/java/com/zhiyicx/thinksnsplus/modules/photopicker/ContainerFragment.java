package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.thinksnsplus.R;

/**
 * User: qii
 * Date: 14-4-30
 */
public class ContainerFragment extends Fragment {

    public static ContainerFragment newInstance(String url, AnimationRect rect,
                                                boolean animationIn, boolean firstOpenPage) {
        ContainerFragment fragment = new ContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        bundle.putBoolean("firstOpenPage", firstOpenPage);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_container_layout, container, false);

        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        boolean animateIn = bundle.getBoolean("animationIn");
        bundle.putBoolean("animationIn", false);
        displayPicture(url, animateIn);
        return view;
    }

    private void displayPicture(String path, boolean animateIn) {
        PhotoViewActivity activity = (PhotoViewActivity) getActivity();
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

        Fragment fragment = GeneralPictureFragment.newInstance(path, rect, animateIn);
        getChildFragmentManager().beginTransaction().replace(R.id.child, fragment)
                .commitAllowingStateLoss();

    }

    public void animationExit(ObjectAnimator backgroundAnimator) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.child);
        GeneralPictureFragment child = (GeneralPictureFragment) fragment;
        child.animationExit(backgroundAnimator);
    }

    public boolean canAnimateCloseActivity() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.child);
        if (fragment instanceof GeneralPictureFragment) {
            return true;
        } else {
            return false;
        }
    }

}
