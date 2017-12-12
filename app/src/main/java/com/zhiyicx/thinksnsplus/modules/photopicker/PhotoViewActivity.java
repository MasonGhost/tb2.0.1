package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.PhotoViewDataCacheBean;

import java.util.ArrayList;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment.COMPLETE_REQUEST_CODE;

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
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected Fragment getFragment() {
        return PhotoViewFragment.newInstance();
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
     * @param tolls                      之前就选中了的照片,可能 size=0
     */
    public static void startToPhotoView(Fragment fragment, ArrayList<String> allPhotos,
                                        ArrayList<String> selectedPhoto
            , ArrayList<AnimationRectBean> animationRectBeanArrayList, int maxCount,
                                        int currentPosition, boolean isToll, ArrayList<ImageBean> tolls) {
        PhotoViewDataCacheBean photoViewDataCacheBean = new PhotoViewDataCacheBean();
        photoViewDataCacheBean.setAllPhotos(allPhotos);
        photoViewDataCacheBean.setSelectedPhoto(selectedPhoto);
        photoViewDataCacheBean.setAnimationRectBeanArrayList(animationRectBeanArrayList);
        photoViewDataCacheBean.setMaxCount(maxCount);
        photoViewDataCacheBean.setCurrentPosition(currentPosition);
        photoViewDataCacheBean.setToll(isToll);
        photoViewDataCacheBean.setSelectedPhotos(tolls);
        // 处理传递数据过大
        SharePreferenceUtils.saveObject(fragment.getContext(), SharePreferenceTagConfig
                .SHAREPREFERENCE_TAG_PHOTO_VIEW_INTNET_CACHE, photoViewDataCacheBean);

        Intent it = new Intent(fragment.getContext(), PhotoViewActivity.class);
        fragment.startActivityForResult(it, COMPLETE_REQUEST_CODE);
    }
}
