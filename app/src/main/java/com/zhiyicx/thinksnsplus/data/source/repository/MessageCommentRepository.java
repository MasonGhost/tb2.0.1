package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class MessageCommentRepository implements MessageCommentContract.Repository {
    private CommonClient mCommonClient;

    public MessageCommentRepository(ServiceManager serviceManager) {
        super();
        mCommonClient = serviceManager.getCommonClient();
    }


}

