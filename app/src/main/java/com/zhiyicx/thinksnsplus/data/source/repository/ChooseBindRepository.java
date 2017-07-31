package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind.ChooseBindContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class ChooseBindRepository extends BaseThirdPlatformRepository implements ChooseBindContract.Repository{

    @Inject
    public ChooseBindRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
