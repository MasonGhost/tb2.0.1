package com.zhiyicx.thinksnsplus.modules.password.changepassword;


import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class ChangePasswordPresenterModule {
    private final ChangePasswordContract.View mView;

    public ChangePasswordPresenterModule(ChangePasswordContract.View view) {
        mView = view;
    }

    @Provides
    ChangePasswordContract.View provideChangePasswordContractView() {
        return mView;
    }

}
