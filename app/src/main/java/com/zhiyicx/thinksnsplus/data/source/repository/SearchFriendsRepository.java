package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.search.SearchFriendsContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */

public class SearchFriendsRepository extends BaseFriendsRepository implements SearchFriendsContract.Repository{

    @Inject
    public SearchFriendsRepository(ServiceManager manager) {
        super(manager);
    }
}
