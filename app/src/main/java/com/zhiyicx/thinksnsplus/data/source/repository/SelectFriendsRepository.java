package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/12
 * @contact email:648129313@qq.com
 */

public class SelectFriendsRepository extends BaseFriendsRepository implements SelectFriendsContract.Repository{

    @Inject
    public SelectFriendsRepository(ServiceManager manager) {
        super(manager);
    }
}
