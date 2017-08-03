package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.third_platform.complete.CompleteAccountContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class CompleteAccountRepository extends BaseThirdPlatformRepository implements CompleteAccountContract.Repository{

    @Inject
    public CompleteAccountRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
