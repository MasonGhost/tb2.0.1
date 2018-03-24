package com.zhiyicx.thinksnsplus.modules.tb.message;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lx on 2018/3/24.
 */
@Module
public class MessageListPresenterModule {
    MessageListContract.View mView;
    public MessageListPresenterModule(MessageListContract.View view) {
        mView = view;
    }

    @Provides
    MessageListContract.View provideMessageListContractView() {
        return mView;
    }
}
