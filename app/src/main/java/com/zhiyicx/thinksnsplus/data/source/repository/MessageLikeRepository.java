package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelike.MessageLikeContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class MessageLikeRepository implements MessageLikeContract.Repository {
    private CommonClient mCommonClient;

    public MessageLikeRepository(ServiceManager serviceManager) {
        super();
        mCommonClient = serviceManager.getCommonClient();
    }


}
