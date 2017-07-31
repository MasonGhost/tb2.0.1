package com.zhiyicx.thinksnsplus.modules.settings.account;

import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountManagementPresenter extends BasePresenter<AccountManagementContract.Repository, AccountManagementContract.View>
        implements AccountManagementContract.Presenter{

    @Inject
    public AccountManagementPresenter(AccountManagementContract.Repository repository,
                                      AccountManagementContract.View rootView) {
        super(repository, rootView);
    }
}
