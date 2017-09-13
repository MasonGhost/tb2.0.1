package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.personal_center.portrait.HeadPortraitViewContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */

public class HeadPortraitRepository implements HeadPortraitViewContract.Repository {

    private CommonClient mClient;

    @Inject
    public HeadPortraitRepository(ServiceManager manager) {
        mClient = manager.getCommonClient();
    }
}
