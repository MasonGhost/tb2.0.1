package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */

public class CertificationInputRepository implements CertificationInputContract.Repository{

    private UserInfoClient mUserInfoClient;

    @Inject
    public CertificationInputRepository(ServiceManager manager) {
        mUserInfoClient = manager.getUserInfoClient();
    }
}
