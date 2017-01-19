package com.zhiyicx.thinksnsplus.modules.register;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IAuthRepository;
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
    RegisterContract.Repository provideRegisterContractRepository(ServiceManager serviceManager, Application application){
        return new RegisterRepository(serviceManager,application);
    }
    @Provides
    IAuthRepository provideIAuthRepository(ServiceManager serviceManager, Application application){
        return new AuthRepository(serviceManager,application);
    }
}
