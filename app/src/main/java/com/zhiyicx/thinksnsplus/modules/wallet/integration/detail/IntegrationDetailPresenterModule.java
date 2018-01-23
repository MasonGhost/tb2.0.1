package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class IntegrationDetailPresenterModule {
    IntegrationDetailContract.View mView;

    public IntegrationDetailPresenterModule(IntegrationDetailContract.View view) {
        mView = view;
    }

    @Provides
    IntegrationDetailContract.View provideContractView() {
        return mView;
    }

}
