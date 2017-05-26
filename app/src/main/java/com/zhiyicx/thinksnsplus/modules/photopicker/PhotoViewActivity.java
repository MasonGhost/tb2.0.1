package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

import java.util.ArrayList;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment
        .EXTRA_MAX_COUNT;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment
        .EXTRA_VIEW_ALL_PHOTOS;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment
        .EXTRA_VIEW_INDEX;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment
        .EXTRA_VIEW_SELECTED_PHOTOS;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment
        .COMPLETE_REQUEST_CODE;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewFragment.OLDTOLL;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewFragment.RIGHTTITLE;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/7
 * @contact email:450127106@qq.com
 */
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
        SparseArray<Toll> tolls=bundle.getSparseParcelableArray(OLDTOLL);
        boolean isToll = bundle.getBoolean(RIGHTTITLE, false);
        ArrayList<AnimationRectBean> animationRectBeen = bundle.getParcelableArrayList("rect");
        return PhotoViewFragment.newInstance(selectedPhotos, allPhotos, animationRectBeen, index,
                maxCount, isToll,tolls);
    }

    @Override
    public void onBackPressed() {
        ((PhotoViewFragment) mContanierFragment).backPress();
    }

    public void superBackpress() {
        PhotoViewActivity.super.onBackPressed();
    }

    /**
     * @param fragment                   从哪个fragment跳转过来
     * @param allPhotos                  预览时的所有的图片
     * @param selectedPhoto              预览时的被选择的图片
     * @param animationRectBeanArrayList 图片的rect属性，用于转场的缩放动画
     * @param maxCount                   能够选择的最大数量
     * @param currentPosition            进入预览时，需要显示第几张图片
     * @param isToll                     是否有收费选项
     */
    public static void startToPhotoView(Fragment fragment, ArrayList<String> allPhotos,
                                        ArrayList<String> selectedPhoto
            , ArrayList<AnimationRectBean> animationRectBeanArrayList, int maxCount,
                                        int currentPosition, boolean isToll, SparseArray<Toll> tolls) {
        Intent it = new Intent(fragment.getContext(), PhotoViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_VIEW_INDEX, currentPosition);
        bundle.putBoolean(RIGHTTITLE, isToll);
        bundle.putStringArrayList(EXTRA_VIEW_ALL_PHOTOS, allPhotos);
        bundle.putStringArrayList(EXTRA_VIEW_SELECTED_PHOTOS, selectedPhoto);
        bundle.putParcelableArrayList("rect", animationRectBeanArrayList);
        bundle.putInt(EXTRA_MAX_COUNT, maxCount);
        bundle.putSparseParcelableArray(OLDTOLL, tolls);
        it.putExtras(bundle);
        fragment.startActivityForResult(it, COMPLETE_REQUEST_CODE);
    }
}
