package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
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


}
