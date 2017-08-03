package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.third_platform.bind.BindOldAccountContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class BindOldAccountRepository extends BaseThirdPlatformRepository implements BindOldAccountContract.Repository{

    @Inject
    public BindOldAccountRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
