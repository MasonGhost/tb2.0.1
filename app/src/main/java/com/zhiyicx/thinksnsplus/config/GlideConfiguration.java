package com.zhiyicx.thinksnsplus.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.zhiyicx.common.utils.FileUtils;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @Describe Glide配置信息
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class GlideConfiguration implements GlideModule {
    private static final int IMAGE_DISK_CACHE_MAX_SIZE = 250 * 1024 * 1024;//图片缓存文件最大值为200Mb

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
       /* builder.setDiskCache(new DiskCache.StreamFactory() {
            @Override
            public DiskCache build() {
                // Careful: the external cache directory doesn't enforce permissions
                return DiskLruCacheWrapper.get(FileUtils.getCacheFile(context), IMAGE_DISK_CACHE_MAX_SIZE);
            }
        });*/
        builder.setDiskCache(new DiskLruCacheFactory(FileUtils.getCacheFile(context, false).getAbsolutePath() + "/glide_cache", IMAGE_DISK_CACHE_MAX_SIZE));

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (0.3 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (0.3 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(new TSImageRetryIntercepter(3))
        .readTimeout(15,TimeUnit.SECONDS);
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);
        glide.register(GlideUrl.class, InputStream.class, factory);
    }
}
