package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.settings.account.AccountManagementContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountManagementRepository implements AccountManagementContract.Repository {

    @Inject
    public AccountManagementRepository(ServiceManager serviceManager) {
    }
}
