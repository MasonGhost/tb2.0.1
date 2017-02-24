package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicContract;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/21
 * @contact email:450127106@qq.com
 */

public class SendDynamicRepository extends BaseDynamicRepository implements SendDynamicContract.Repository {

    @Inject
    public SendDynamicRepository(ServiceManager serviceManager, Application context) {
        super(serviceManager,context);
    }

}
