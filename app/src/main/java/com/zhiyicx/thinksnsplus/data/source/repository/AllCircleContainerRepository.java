package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.container.AllCircleContainerContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:53
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllCircleContainerRepository extends BaseCircleRepository implements AllCircleContainerContract.Repository {

    @Inject
    public AllCircleContainerRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
