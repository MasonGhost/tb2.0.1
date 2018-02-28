package com.zhiyicx.thinksnsplus.modules.wallet.tb;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/02/28/19:23
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class WalletPresenterModule {
    WalletContract.View mView;

    public WalletPresenterModule(WalletContract.View view) {
        mView = view;
    }

    @Provides
    WalletContract.View provideWalletContractView(){
        return mView;
    }
}
