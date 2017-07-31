package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe 第三方登陆相关
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class BaseThirdPlatformRepository implements IThirdPlatformRepository{

    @Inject
    public BaseThirdPlatformRepository(ServiceManager serviceManager) {
    }
}
