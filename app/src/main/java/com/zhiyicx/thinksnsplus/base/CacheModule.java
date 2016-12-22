package com.zhiyicx.thinksnsplus.base;


import com.zhiyicx.thinksnsplus.data.source.local.CommonCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache.internal.RxCache;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/21
 * @Contact master.jungle68@gmail.com
 */

@Module
public class CacheModule {

    @Singleton
    @Provides
    CommonCache provideCommonService(RxCache rxCache) {
        return rxCache.using(CommonCache.class);
    }


}
