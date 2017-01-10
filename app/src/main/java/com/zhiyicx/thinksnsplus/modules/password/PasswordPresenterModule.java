package com.zhiyicx.thinksnsplus.modules.password;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.PasswordRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class PasswordPresenterModule {
    private final PasswordContract.View mView;

    public PasswordPresenterModule(PasswordContract.View view) {
        mView = view;
    }

    @Provides
    PasswordContract.View providePasswordContractView() {
        return mView;
    }


    @Provides
    PasswordContract.Repository providePasswordContractRepository(ServiceManager serviceManager) {
        return new PasswordRepository(serviceManager);
    }
}
