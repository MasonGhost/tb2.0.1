package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.channel.group_dynamic.dig_list.GroupDigListContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */

public class GroupDigListRepository extends BaseChannelRepository implements GroupDigListContract.Repository{

    @Inject
    public GroupDigListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
