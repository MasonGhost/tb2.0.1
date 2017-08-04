package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public class CertificationDetailRepository implements CertificationDetailContract.Repository{

    private UserInfoClient mUserInfoClient;

    @Inject
    public CertificationDetailRepository(ServiceManager manager) {
        mUserInfoClient = manager.getUserInfoClient();
    }
}
