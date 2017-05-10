package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class MessageCommentRepository extends UserInfoRepository implements MessageCommentContract.Repository {


    public MessageCommentRepository(ServiceManager serviceManager, Application application) {
        super(serviceManager, application);
    }
}
