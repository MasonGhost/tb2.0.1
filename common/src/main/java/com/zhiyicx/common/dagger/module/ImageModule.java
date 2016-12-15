package com.zhiyicx.common.dagger.module;

import com.jess.arms.widget.imageloader.BaseImageLoaderStrategy;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.jess.arms.widget.imageloader.glide.GlideImageLoaderStrategy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */
@Module
public class ImageModule {

    @Singleton
    @Provides
    public BaseImageLoaderStrategy provideImageLoaderStrategy() {
        return new GlideImageLoaderStrategy();
    }

    @Singleton
    @Provides
    public ImageLoader provideImageLoader(BaseImageLoaderStrategy strategy) {
        return new ImageLoader(strategy);
    }
}
