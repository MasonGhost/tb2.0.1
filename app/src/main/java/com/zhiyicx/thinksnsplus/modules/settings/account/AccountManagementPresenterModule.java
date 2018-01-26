package com.zhiyicx.thinksnsplus.modules.settings.account;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */
@Module
public class AccountManagementPresenterModule {

    private AccountManagementContract.View mView;

    public AccountManagementPresenterModule(AccountManagementContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public AccountManagementContract.View provideAccountManagementContractView(){
        return mView;
    }

}
