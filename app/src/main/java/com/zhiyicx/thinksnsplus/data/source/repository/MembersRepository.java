package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.manager.members.MembersContract;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MembersRepository extends BaseCircleRepository implements MembersContract.Repository {

    @Inject
    public MembersRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
