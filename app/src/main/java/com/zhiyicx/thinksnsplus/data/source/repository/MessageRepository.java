package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.MessageContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class MessageRepository implements MessageContract.Repository {
    private CommonClient mCommonClient;

    public MessageRepository(ServiceManager serviceManager) {
        super();
        mCommonClient = serviceManager.getCommonClient();
    }


}
