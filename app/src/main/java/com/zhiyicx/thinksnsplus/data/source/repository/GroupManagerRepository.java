package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.chat.edit.manager.GroupManagerContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class GroupManagerRepository extends BaseFriendsRepository implements GroupManagerContract.Repository{

    @Inject
    public GroupManagerRepository(ServiceManager manager) {
        super(manager);
    }
}
