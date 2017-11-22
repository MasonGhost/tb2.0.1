package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailContract;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:38
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleDetailRepository extends BaseCircleRepository implements CircleDetailContract.Repository {

    @Inject
    public CircleDetailRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
