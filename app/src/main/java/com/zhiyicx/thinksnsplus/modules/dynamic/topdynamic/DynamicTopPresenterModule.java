package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/05/23/13:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class DynamicTopPresenterModule {

    DynamicTopContract.View mView;

    public DynamicTopPresenterModule(DynamicTopContract.View view) {
        this.mView = view;
    }

    @Provides
    DynamicTopContract.View provideDynamicTopContractView() {
        return mView;
    }

}
