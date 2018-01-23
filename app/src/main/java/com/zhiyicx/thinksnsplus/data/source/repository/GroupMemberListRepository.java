package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.chat.edit.manager.GroupManagerContract;
import com.zhiyicx.thinksnsplus.modules.chat.member.GroupMemberListContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class GroupMemberListRepository extends BaseFriendsRepository implements GroupMemberListContract.Repository{

    @Inject
    public GroupMemberListRepository(ServiceManager manager) {
        super(manager);
    }
}
