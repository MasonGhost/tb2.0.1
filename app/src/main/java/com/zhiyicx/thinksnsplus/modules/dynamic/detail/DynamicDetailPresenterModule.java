package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.DynamicDetailRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */
@Module
public class DynamicDetailPresenterModule {
    private DynamicDetailContract.View mView;

    public DynamicDetailPresenterModule(DynamicDetailContract.View view) {
        mView = view;
    }

    @Provides
    public DynamicDetailContract.View provideDynamicDetailContractView() {
        return mView;
    }

    @Provides
    public DynamicDetailContract.Repository provideDynamicDetailContractRepository(ServiceManager serviceManager, Application application) {
        return new DynamicDetailRepository(serviceManager, application);
    }
}
