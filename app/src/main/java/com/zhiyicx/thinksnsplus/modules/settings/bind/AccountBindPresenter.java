package com.zhiyicx.thinksnsplus.modules.settings.bind;

import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountBindPresenter extends BasePresenter<AccountBindContract.Repository, AccountBindContract.View>
        implements AccountBindContract.Presenter{

    @Inject
    public AccountBindPresenter(AccountBindContract.Repository repository, AccountBindContract.View rootView) {
        super(repository, rootView);
    }
}
