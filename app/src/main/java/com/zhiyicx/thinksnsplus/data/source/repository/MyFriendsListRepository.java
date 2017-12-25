package com.zhiyicx.thinksnsplus.data.source.repository;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.MyFriendsListContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */

public class MyFriendsListRepository extends BaseFriendsRepository implements MyFriendsListContract.Repository{

    @Inject
    public MyFriendsListRepository(ServiceManager manager) {
        super(manager);
    }
}
