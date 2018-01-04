package com.zhiyicx.thinksnsplus.modules.register;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */
@Module
public class RegisterPresenterModule {
    private final RegisterContract.View mView;

    public RegisterPresenterModule(RegisterContract.View view) {
        mView = view;
    }

    @Provides
    RegisterContract.View provideRegisterContractView() {
        return mView;
    }


}
