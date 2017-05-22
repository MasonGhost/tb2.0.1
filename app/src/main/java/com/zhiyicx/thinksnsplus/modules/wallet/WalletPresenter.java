package com.zhiyicx.thinksnsplus.modules.wallet;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class WalletPresenter extends AppBasePresenter<WalletContract.Repository, WalletContract.View> implements WalletContract.Presenter {

    @Inject
    AuthRepository mIAuthRepository;

    @Inject
    public WalletPresenter(WalletContract.Repository repository, WalletContract.View rootView) {
        super(repository, rootView);
    }

}
