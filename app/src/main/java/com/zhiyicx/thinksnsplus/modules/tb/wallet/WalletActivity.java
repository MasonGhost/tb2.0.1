package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author Jliuer
 * @Date 2018/02/28/19:20
 * @Email Jliuer@aliyun.com
 * @Description tb 钱包
 */
public class WalletActivity extends TSActivity<WalletPresenter, WalletFragment> {
    @Override
    protected WalletFragment getFragment() {
        return new WalletFragment();
    }

    @Override
    protected void componentInject() {
        DaggerWalletComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .walletPresenterModule(new WalletPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
