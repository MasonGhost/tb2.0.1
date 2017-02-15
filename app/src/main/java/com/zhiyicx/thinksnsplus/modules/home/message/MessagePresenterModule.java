package com.zhiyicx.thinksnsplus.modules.home.message;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;

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


    @Provides
    MessageContract.Repository provideMessageContractRepository(ServiceManager serviceManager) {
        return new MessageRepository(serviceManager);
    }
    @Provides
    ChatContract.Repository provideChatContractRepository(ServiceManager serviceManager, Application application){
        return new ChatRepository(serviceManager,application);
    }
}
