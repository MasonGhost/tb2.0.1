package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.app.Application;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IAuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IUploadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
@Module
public class UserInfoPresenterModule {
    private UserInfoContract.View mView;

    public UserInfoPresenterModule(UserInfoContract.View view) {
        mView = view;
    }

    @Provides
    UserInfoContract.View provideUserInfoContractView() {
        return mView;
    }

    @Provides
    UserInfoContract.Repository provideUserInfoContractRepository(ServiceManager serviceManager) {
        return new UserInfoRepository(serviceManager);
    }

    @Provides
    IUploadRepository provideIUploadRepository(ServiceManager serviceManager, Application application) {
        return new UpLoadRepository(serviceManager, application);
    }
    @Provides
    IAuthRepository provideIAuthRepository(ServiceManager serviceManager, Application application) {
        return new AuthRepository(serviceManager, application);
    }
}
