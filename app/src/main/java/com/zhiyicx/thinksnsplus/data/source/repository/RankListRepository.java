package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.rank.main.list.RankListContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class RankListRepository implements RankListContract.Repository{

    @Inject
    public RankListRepository(ServiceManager manager) {
    }
}
