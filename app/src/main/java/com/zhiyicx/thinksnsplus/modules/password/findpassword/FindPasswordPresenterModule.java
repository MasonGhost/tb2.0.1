<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.FindPasswordRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class FindPasswordPresenterModule {
    private final FindPasswordContract.View mView;

    public FindPasswordPresenterModule(FindPasswordContract.View view) {
        mView = view;
    }

    @Provides
    FindPasswordContract.View provideFindPasswordContractView() {
        return mView;
    }


    @Provides
    FindPasswordContract.Repository provideFindPasswordContractRepository(ServiceManager serviceManager) {
        return new FindPasswordRepository(serviceManager);
    }
}
=======
package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.FindPasswordRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class FindPasswordPresenterModule {
    private final FindPasswordContract.View mView;

    public FindPasswordPresenterModule(FindPasswordContract.View view) {
        mView = view;
    }

    @Provides
    FindPasswordContract.View provideFindPasswordContractView() {
        return mView;
    }


    @Provides
    FindPasswordContract.Repository provideFindPasswordContractRepository(ServiceManager serviceManager) {
        return new FindPasswordRepository(serviceManager);
    }
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
