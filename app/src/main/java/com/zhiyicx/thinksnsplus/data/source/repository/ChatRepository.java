package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.local.CacheManager;
import com.zhiyicx.thinksnsplus.data.source.local.CommonCache;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class ChatRepository implements ChatContract.Repository {
    private CommonClient mCommonClient;
    private CommonCache mCommonCache;

    public ChatRepository(ServiceManager serviceManager, CacheManager cacheManager) {
        super();
        mCommonClient = serviceManager.getCommonClient();
        mCommonCache = cacheManager.getCommonCache();
    }

}
