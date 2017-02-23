<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.chat;

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
    ChatContract.Repository provideChatContractRepository(ServiceManager serviceManager){
        return new ChatRepository(serviceManager);
    }
}
=======
package com.zhiyicx.thinksnsplus.modules.chat;

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
    ChatContract.Repository provideChatContractRepository(ServiceManager serviceManager){
        return new ChatRepository(serviceManager);
    }
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
