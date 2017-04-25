package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.system_conversation.SystemConversationContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class SystemConversationRepository extends SystemRepository implements SystemConversationContract.Repository {


    public SystemConversationRepository(ServiceManager serviceManager, Application application) {
        super(serviceManager, application);
    }
}
