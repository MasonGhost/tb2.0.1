package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleContract;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/11/21/17:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CreateCircleRepository extends BaseCircleRepository implements CreateCircleContract.Repository {

    @Inject
    public CreateCircleRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
