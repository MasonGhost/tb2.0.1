package com.zhiyicx.thinksnsplus.modules.home.message;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017//13
 * @Contact master.jungle68@gmail.com
 */
@Module
public class MessagePresenterModule {
    private final MessageContract.View mView;

    public MessagePresenterModule(MessageContract.View view) {
        mView = view;
    }

    @Provides
    MessageContract.View provideMessageContractView() {
        return mView;
    }

}
