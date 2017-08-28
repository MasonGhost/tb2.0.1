package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.dig.InfoDigListContract.Repository;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/11
 * @contact email:648129313@qq.com
 */

public class InfoDigListRepository extends BaseInfoRepository implements Repository{

    @Inject
    public InfoDigListRepository(ServiceManager manager) {
        super(manager);
    }
}
