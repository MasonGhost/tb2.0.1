package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelike.MessageLikeContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class MessageLikeRepository extends UserInfoRepository implements MessageLikeContract.Repository {


    public MessageLikeRepository(ServiceManager serviceManager, Application application) {
        super(serviceManager, application);
    }
}
