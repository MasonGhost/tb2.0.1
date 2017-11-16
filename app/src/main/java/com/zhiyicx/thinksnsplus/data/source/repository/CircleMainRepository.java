package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ChannelClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.main.CircleMainContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/11/14/11:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainRepository extends BaseChannelRepository implements CircleMainContract.Repository {

    ChannelClient mChannelClient;

    @Inject
    public CircleMainRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mChannelClient = serviceManager.getChannelClient();
    }
}
