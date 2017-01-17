package com.zhiyicx.baseproject.impl.photoselector;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/17
 * @contact email:450127106@qq.com
 */
@Module
public class PhotoSeletorImplModule {
    private PhotoSelectorImpl.IPhotoBackListener mPhotoBackListener;
    private Fragment mFragment;

    public PhotoSeletorImplModule(PhotoSelectorImpl.IPhotoBackListener photoBackListener, Fragment fragment) {
        mPhotoBackListener = photoBackListener;
        this.mFragment = fragment;
    }

    @Provides
    public PhotoSelectorImpl providePhotoSelectorImpl() {
        return new PhotoSelectorImpl(mPhotoBackListener, mFragment);
    }
}
