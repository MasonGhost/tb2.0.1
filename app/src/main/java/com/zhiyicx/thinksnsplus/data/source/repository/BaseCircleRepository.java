package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BaseCircleRepository implements IBaseCircleRepository {

    protected CircleClient mCircleClient;

    @Inject
    public BaseCircleRepository(ServiceManager serviceManager) {
        mCircleClient = serviceManager.getCircleClient();
    }
}
