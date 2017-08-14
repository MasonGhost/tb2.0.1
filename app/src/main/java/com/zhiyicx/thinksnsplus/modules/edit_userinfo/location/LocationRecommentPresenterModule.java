package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IUploadRepository;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
@Module
public class LocationRecommentPresenterModule {
    private UserInfoContract.View mView;

    public LocationRecommentPresenterModule(UserInfoContract.View view) {
        mView = view;
    }

    @Provides
    UserInfoContract.View provideUserInfoContractView() {
        return mView;
    }

    @Provides
    UserInfoContract.Repository provideUserInfoContractRepository(ServiceManager serviceManager, Application application) {
        return new UserInfoRepository(serviceManager,application);
    }

    @Provides
    IUploadRepository provideIUploadRepository(ServiceManager serviceManager, Application application) {
        return new UpLoadRepository(serviceManager, application);
    }

}
