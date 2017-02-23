<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.guide;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.GuideRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IAuthRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class GuidePresenterModule {

    private final GuideContract.View mView;

    public GuidePresenterModule(GuideContract.View view) {
        mView = view;
    }

    @Provides
    GuideContract.View provideGuideContractView() {
        return mView;
    }


    @Provides
    GuideContract.Repository provideGuideContractRepository(ServiceManager serviceManager){
        return new GuideRepository(serviceManager);
    }

    @Provides
    IAuthRepository provideIAuthRepository(AuthRepository authRepository) {
        return authRepository;
    }
}
=======
package com.zhiyicx.thinksnsplus.modules.guide;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.GuideRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.IAuthRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class GuidePresenterModule {

    private final GuideContract.View mView;

    public GuidePresenterModule(GuideContract.View view) {
        mView = view;
    }

    @Provides
    GuideContract.View provideGuideContractView() {
        return mView;
    }


    @Provides
    GuideContract.Repository provideGuideContractRepository(ServiceManager serviceManager){
        return new GuideRepository(serviceManager);
    }

    @Provides
    IAuthRepository provideIAuthRepository(AuthRepository authRepository) {
        return authRepository;
    }
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
