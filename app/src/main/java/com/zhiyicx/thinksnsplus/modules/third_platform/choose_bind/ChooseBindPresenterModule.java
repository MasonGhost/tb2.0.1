package com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@Module
public class ChooseBindPresenterModule {

    private ChooseBindContract.View mView;

    public ChooseBindPresenterModule(ChooseBindContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public ChooseBindContract.View provideChooseBindContractView(){
        return mView;
    }

}
