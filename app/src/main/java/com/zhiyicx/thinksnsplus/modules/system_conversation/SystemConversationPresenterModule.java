package com.zhiyicx.thinksnsplus.modules.system_conversation;

import com.zhiyicx.thinksnsplus.data.source.repository.SystemConversationRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */
@Module
public class SystemConversationPresenterModule {
    private final SystemConversationContract.View mView;

    public SystemConversationPresenterModule(SystemConversationContract.View view) {
        mView = view;
    }

    @Provides
    SystemConversationContract.View provideChatContractView() {
        return mView;
    }

    @Provides
    SystemConversationContract.Repository provideSystemConversationContractRepository(SystemConversationRepository systemConversationRepository){
        return systemConversationRepository;
    }
}
