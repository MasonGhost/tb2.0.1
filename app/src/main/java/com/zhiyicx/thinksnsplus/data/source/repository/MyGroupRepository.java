package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.mine.MyGroupContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/25
 * @contact email:648129313@qq.com
 */

public class MyGroupRepository extends BaseChannelRepository implements MyGroupContract.Repository{

    @Inject
    public MyGroupRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
