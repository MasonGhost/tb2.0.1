package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.manager.earning.CircleEarningContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/12/12/13:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleEarningRepository extends BaseCircleRepository implements CircleEarningContract.Repository {

    @Inject
    public CircleEarningRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
