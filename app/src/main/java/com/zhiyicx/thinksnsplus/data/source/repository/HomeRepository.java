package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.HomeContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class HomeRepository implements HomeContract.Repository {
    private CommonClient mCommonClient;

    public HomeRepository(ServiceManager serviceManager) {
        super();
        mCommonClient = serviceManager.getCommonClient();
    }


}
