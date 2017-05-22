package com.zhiyicx.thinksnsplus.modules.wallet;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 钱包
 * @Author Jungle68
 * @Date 2017/5/22
 * @Contact master.jungle68@gmail.com
 */
public class WalletActivity extends TSActivity<WalletPresenter, WalletFragment> {


    @Override
    protected void componentInject() {
        DaggerWalletComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .walletPresenterModule(new WalletPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected WalletFragment getFragment() {
        return WalletFragment.newInstance();
    }

}
