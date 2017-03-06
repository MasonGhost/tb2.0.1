package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.DigListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */
@Module
public class DigListPresenterModule {
    private DigListContract.View mView;

    public DigListPresenterModule(DigListContract.View view) {
        mView = view;
    }

    @Provides
    DigListContract.View provideDigListContractView() {
        return mView;
    }

    @Provides
    DigListContract.Repository provideDigListContractRepository(ServiceManager serviceManager, Application application) {
        return new DigListRepository(serviceManager, application);
    }
}
