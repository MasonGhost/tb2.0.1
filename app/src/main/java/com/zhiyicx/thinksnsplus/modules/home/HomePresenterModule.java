package com.zhiyicx.thinksnsplus.modules.home;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.HomeRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017//13
 * @Contact master.jungle68@gmail.com
 */
@Module
public class HomePresenterModule {
    private final HomeContract.View mView;

    public HomePresenterModule(HomeContract.View view) {
        mView = view;
    }

    @Provides
    HomeContract.View provideMessageContractView() {
        return mView;
    }


    @Provides
    HomeContract.Repository provideMessageContractRepository(ServiceManager serviceManager) {
        return new HomeRepository(serviceManager);
    }

}
