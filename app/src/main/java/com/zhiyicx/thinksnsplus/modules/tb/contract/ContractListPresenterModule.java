package com.zhiyicx.thinksnsplus.modules.tb.contract;


import dagger.Module;
import dagger.Provides;

@Module
public class ContractListPresenterModule {
    ContractListContract.View mView;
    public ContractListPresenterModule(ContractListContract.View view) {
        mView = view;
    }

    @Provides
    ContractListContract.View provideContractListContractView() {
        return mView;
    }
}
