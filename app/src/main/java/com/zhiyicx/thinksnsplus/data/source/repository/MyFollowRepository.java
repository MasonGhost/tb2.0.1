package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.follow.MyFollowContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyFollowRepository extends BaseQARepository implements MyFollowContract.Repository{

    @Inject
    public MyFollowRepository(ServiceManager manager) {
        super(manager);
    }
}
