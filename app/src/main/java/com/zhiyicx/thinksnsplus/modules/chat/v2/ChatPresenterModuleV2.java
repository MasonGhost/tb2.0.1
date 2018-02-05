package com.zhiyicx.thinksnsplus.modules.chat.v2;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */
@Module
public class ChatPresenterModuleV2 {
    private final ChatContractV2.View mView;

    public ChatPresenterModuleV2(ChatContractV2.View view) {
        mView = view;
    }

    @Provides
    ChatContractV2.View provideChatContractView() {
        return mView;
    }

}
