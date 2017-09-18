package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.settings.init_password.InitPasswordContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/18
 * @contact email:648129313@qq.com
 */

public class InitPasswordRepository implements InitPasswordContract.Repository{

    private PasswordClient mClient;

    @Inject
    public InitPasswordRepository(ServiceManager serviceManager) {
        mClient = serviceManager.getPasswordClient();
    }
}
