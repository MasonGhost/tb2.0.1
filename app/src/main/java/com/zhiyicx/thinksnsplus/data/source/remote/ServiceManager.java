package com.zhiyicx.thinksnsplus.data.source.remote;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

@Singleton
public class ServiceManager {
    private CommonClient mCommonClient;

    /**
     * 如果需要添加 service 只需在构造方法中添加对应的 service,在提供 get 方法返回出去,只要在 ServiceModule 提供了该 service
     * Dagger2 会自行注入
     * @param commonClient
     */
    @Inject public ServiceManager(CommonClient commonClient){
        this.mCommonClient = commonClient;
    }

    public CommonClient getCommonClient() {
        return mCommonClient;
    }
}
