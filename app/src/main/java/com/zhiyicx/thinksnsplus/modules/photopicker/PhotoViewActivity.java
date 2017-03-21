package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

import java.util.ArrayList;
import java.util.List;

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
        ArrayList<AnimationRectBean> animationRectBeen = bundle.getParcelableArrayList("rect");
        PhotoViewFragment imagePagerFragment =
                PhotoViewFragment.newInstance(selectedPhotos, allPhotos, animationRectBeen, index, maxCount);
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
