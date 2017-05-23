package com.zhiyicx.thinksnsplus.modules.dynamic.top;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.DynamicTopRepsotory;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/05/23/13:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class DynamicPresenterModule {
    DynamicTopContract.View mView;

    public DynamicPresenterModule(DynamicTopContract.View view) {
        this.mView = view;
    }

    @Provides
    public DynamicTopContract.View provideDynamicTopContractView() {
        return mView;
    }

    @Provides
    public DynamicTopContract.Repository provideDynamicTopContractRepository(ServiceManager serviceManager) {
        return new DynamicTopRepsotory(serviceManager);
    }
}
