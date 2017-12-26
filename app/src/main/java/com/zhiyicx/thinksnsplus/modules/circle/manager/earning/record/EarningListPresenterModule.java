package com.zhiyicx.thinksnsplus.modules.circle.manager.earning.record;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class EarningListPresenterModule {
    EarningListContract.View mView;

    public EarningListPresenterModule(EarningListContract.View view) {
        mView = view;
    }

    @Provides
    EarningListContract.View provideBillContractView() {
        return mView;
    }

}
