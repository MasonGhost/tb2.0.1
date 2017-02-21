package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017//13
 * @Contact master.jungle68@gmail.com
 */
@Module
public class DynamicPresenterModule {
    private final DynamicContract.View mView;

    public DynamicPresenterModule(DynamicContract.View view) {
        mView = view;
    }

    @Provides
    DynamicContract.View provideMessageContractView() {
        return mView;
    }


    @Provides
    DynamicContract.Repository provideDynamicContractRepository(ServiceManager serviceManager, Application application) {
        return new DynamicContract.Repository() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
    }

}
