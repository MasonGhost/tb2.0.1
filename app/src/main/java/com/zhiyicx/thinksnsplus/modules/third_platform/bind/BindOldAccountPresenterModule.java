package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@Module
public class BindOldAccountPresenterModule {

    private BindOldAccountContract.View mView;

    public BindOldAccountPresenterModule(BindOldAccountContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public BindOldAccountContract.View provideBindOldAccountContractView(){
        return mView;
    }

}
