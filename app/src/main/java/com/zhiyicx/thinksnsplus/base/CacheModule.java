<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.base;


import com.zhiyicx.baseproject.cache.CacheImp;
import com.zhiyicx.baseproject.cache.IDataBaseOperate;

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
    CacheImp provideCommonService(RxCache rxCache) {
        return rxCache.using(CacheImp.class);
    }


}
=======
package com.zhiyicx.thinksnsplus.base;


import com.zhiyicx.baseproject.cache.CacheImp;
import com.zhiyicx.baseproject.cache.IDataBaseOperate;

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
    CacheImp provideCommonService(RxCache rxCache) {
        return rxCache.using(CacheImp.class);
    }


}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
