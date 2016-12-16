package com.zhiyicx.thinksnsplus.data.source.local;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

@Singleton
public class CacheManager{
    private CommonCache mCommonCache;

    /**
     * 如果需要添加 Cache 只需在构造方法中添加对应的 Cache,
     * 在提供 get 方法返回出去,只要在CacheModule提供了该 Cache Dagger2 会自行注入
     * @param commonCache
     */
    @Inject
    public CacheManager(CommonCache commonCache) {
        this.mCommonCache = commonCache;
    }

    public CommonCache getCommonCache() {
        return mCommonCache;
    }
}
