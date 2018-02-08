package com.zhiyicx.thinksnsplus.modules.chat;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */
@Module
public class ChatPresenterModule {
    private final ChatContract.View mView;

    public ChatPresenterModule(ChatContract.View view) {
        mView = view;
    }

    @Provides
    ChatContract.View provideChatContractView() {
        return mView;
    }

}
