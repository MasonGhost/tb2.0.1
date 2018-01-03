package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;


import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class MessageLikePresenterModule {
    private final MessageLikeContract.View mView;

    public MessageLikePresenterModule(MessageLikeContract.View view) {
        mView = view;
    }

    @Provides
    MessageLikeContract.View provideMessageLikeContractView() {
        return mView;
    }

}
