package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
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
}
