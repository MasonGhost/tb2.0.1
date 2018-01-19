package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine;


import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@Module
public class MineIntegrationPresenterModule {
    private final MineIntegrationContract.View mView;

    public MineIntegrationPresenterModule(MineIntegrationContract.View view) {
        mView = view;
    }

    @Provides
    MineIntegrationContract.View provideContractView() {
        return mView;
    }

}
