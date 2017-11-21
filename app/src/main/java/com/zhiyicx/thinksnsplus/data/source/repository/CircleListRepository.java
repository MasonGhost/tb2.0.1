package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.CircleListContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleListRepository extends BaseCircleRepository implements CircleListContract.Repository {

    @Inject
    public CircleListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
