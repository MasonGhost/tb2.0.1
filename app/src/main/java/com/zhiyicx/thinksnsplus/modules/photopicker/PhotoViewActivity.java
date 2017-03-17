package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSActivity;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.fragment.ImagePagerFragment;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment.EXTRA_MAX_COUNT;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment.EXTRA_VIEW_ALL_PHOTOS;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment.EXTRA_VIEW_INDEX;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment.EXTRA_VIEW_SELECTED_PHOTOS;

public class PhotoViewActivity extends TSActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void componentInject() {

    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected Fragment getFragment() {
        Bundle bundle = getIntent().getExtras();
        List<String> allPhotos = bundle.getStringArrayList(EXTRA_VIEW_ALL_PHOTOS);
        List<String> selectedPhotos = bundle.getStringArrayList(EXTRA_VIEW_SELECTED_PHOTOS);
        int index = bundle.getInt(EXTRA_VIEW_INDEX);
        int maxCount = bundle.getInt(EXTRA_MAX_COUNT);
        ArrayList<AnimationRect> animationRects = bundle.getParcelableArrayList("rect");
        PhotoViewFragment imagePagerFragment =
                PhotoViewFragment.newInstance(selectedPhotos, allPhotos, animationRects, index, maxCount);
        return imagePagerFragment;
    }

    @Override
    public void onBackPressed() {
        ((PhotoViewFragment) mContanierFragment).backPress();
    }

    public void superBackpress(){
        PhotoViewActivity.super.onBackPressed();
    }
}
