package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/05/23/14:09
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class WithDrawalsPresenterModule {
    WithDrawalsConstract.View mView;

    public WithDrawalsPresenterModule(WithDrawalsConstract.View view) {
        mView = view;
    }

    @Provides
    WithDrawalsConstract.View proviewWithDrawalsConstractView() {
        return mView;
    }

}
