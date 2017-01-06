package com.zhiyicx.thinksnsplus.modules.chat;

import com.zhiyicx.thinksnsplus.data.source.local.CacheManager;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatRepository;

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


    @Provides
    ChatContract.Repository provideChatContractRepository(ServiceManager serviceManager, CacheManager cacheManager){
        return new ChatRepository(serviceManager,cacheManager);
    }
}
