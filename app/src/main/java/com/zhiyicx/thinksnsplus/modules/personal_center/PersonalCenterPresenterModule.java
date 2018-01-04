package com.zhiyicx.thinksnsplus.modules.personal_center;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */
@Module
public class PersonalCenterPresenterModule {
    private PersonalCenterContract.View mView;

    public PersonalCenterPresenterModule(PersonalCenterContract.View view) {
        mView = view;
    }

    @Provides
    public PersonalCenterContract.View providePersonalCenterContractView() {
        return mView;
    }
}
