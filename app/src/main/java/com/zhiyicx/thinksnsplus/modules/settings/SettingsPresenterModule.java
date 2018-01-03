package com.zhiyicx.thinksnsplus.modules.settings;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class SettingsPresenterModule {
    private final SettingsContract.View mView;

    public SettingsPresenterModule(SettingsContract.View view) {
        mView = view;
    }

    @Provides
    SettingsContract.View provideSettingsContractView() {
        return mView;
    }

}
