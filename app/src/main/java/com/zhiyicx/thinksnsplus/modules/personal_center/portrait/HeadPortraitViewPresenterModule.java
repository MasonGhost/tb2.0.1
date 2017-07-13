package com.zhiyicx.thinksnsplus.modules.personal_center.portrait;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.HeadPortraitRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */
@Module
public class HeadPortraitViewPresenterModule {

    private HeadPortraitViewContract.View mView;

    public HeadPortraitViewPresenterModule(HeadPortraitViewContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public HeadPortraitViewContract.View provideHeadPortraitViewContractView() {
        return mView;
    }

    @Provides
    public HeadPortraitViewContract.Repository provideHeadPortraitRepository(HeadPortraitRepository repository) {
        return repository;
    }

    @Provides
    IUploadRepository provideIUploadRepository(ServiceManager serviceManager, Application application) {
        return new UpLoadRepository(serviceManager, application);
    }
}
