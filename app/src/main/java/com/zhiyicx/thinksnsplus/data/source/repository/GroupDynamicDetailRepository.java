package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.GroupDynamicDetailContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/18
 * @contact email:648129313@qq.com
 */

public class GroupDynamicDetailRepository extends BaseChannelRepository implements GroupDynamicDetailContract.Repository{

    @Inject
    public GroupDynamicDetailRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }


}
