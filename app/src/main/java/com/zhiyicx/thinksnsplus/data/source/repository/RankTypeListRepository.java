package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.rank.type_list.RankTypeListContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public class RankTypeListRepository extends BaseRankRepository implements RankTypeListContract.Repository{

    @Inject
    public RankTypeListRepository(ServiceManager manager) {
        super(manager);
    }
}
