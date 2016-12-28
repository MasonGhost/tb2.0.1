package com.zhiyicx.thinksnsplus.modules.register;

import com.zhiyicx.thinksnsplus.data.source.local.CacheManager;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.RegisterRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */
@Module
public class RegisterPresenterModule {
    private final RegisterContract.View mView;

    public RegisterPresenterModule(RegisterContract.View view) {
        mView = view;
    }

    @Provides
    RegisterContract.View provideRegisterContractView() {
        return mView;
    }


    @Provides
    RegisterContract.Repository provideRegisterContractRepository(ServiceManager serviceManager, CacheManager cacheManager){
        return new RegisterRepository(serviceManager,cacheManager);
    }
}
