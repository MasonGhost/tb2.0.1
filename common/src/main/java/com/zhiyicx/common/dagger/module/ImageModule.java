package com.zhiyicx.common.dagger.module;


import com.zhiyicx.common.utils.imageloader.core.GlideImageLoaderStrategy;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.imageloader.loadstrategy.ImageLoaderStrategy;

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
    public ImageLoaderStrategy provideImageLoaderStrategy() {
        return new GlideImageLoaderStrategy();
    }

    @Singleton
    @Provides
    public ImageLoader provideImageLoader(ImageLoaderStrategy strategy) {
        return new ImageLoader(strategy);
    }
}
