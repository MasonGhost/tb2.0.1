package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class BillPresenterModule {
    BillContract.View mView;

    public BillPresenterModule(BillContract.View view) {
        mView = view;
    }

    @Provides
    BillContract.View provideBillContractView() {
        return mView;
    }

    @Provides
    BillContract.Repository provideBillContractRepository(BillRepository repository) {
        return repository;
    }
}
