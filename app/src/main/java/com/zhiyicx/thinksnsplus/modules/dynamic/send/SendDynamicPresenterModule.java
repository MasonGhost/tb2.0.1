package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseChannelRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseChannelRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SendDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/21
 * @contact email:450127106@qq.com
 */
@Module
public class SendDynamicPresenterModule {
    private SendDynamicContract.View mView;

    public SendDynamicPresenterModule(SendDynamicContract.View view) {
        mView = view;
    }

    @Provides
    SendDynamicContract.View provideSendDynamicContractView() {
        return mView;
    }

    @Provides
    SendDynamicContract.Repository provideSendDynamicPresenterRepository(ServiceManager serviceManager) {
        return new SendDynamicRepository(serviceManager);
    }

    @Provides
    IUploadRepository provideIUploadRepository(ServiceManager serviceManager, Application application) {
        return new UpLoadRepository(serviceManager, application);
    }

    @Provides
    IBaseChannelRepository provideIBaseChannelRepository(IBaseChannelRepository repository){
        return repository;
    }

}
