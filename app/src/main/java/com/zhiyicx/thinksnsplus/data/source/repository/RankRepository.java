package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.rank.RankContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public class RankRepository extends UserInfoRepository implements RankContract.Repository{

    public RankRepository(ServiceManager serviceManager, Application application) {
        super(serviceManager, application);
    }
}
