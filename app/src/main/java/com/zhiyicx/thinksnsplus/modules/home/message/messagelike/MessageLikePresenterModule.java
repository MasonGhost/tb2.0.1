<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageLikeRepository;

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


    @Provides
    MessageLikeContract.Repository provideMessageLikeContractRepository(ServiceManager serviceManager) {
        return new MessageLikeRepository(serviceManager);
    }
}
=======
package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageLikeRepository;

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


    @Provides
    MessageLikeContract.Repository provideMessageLikeContractRepository(ServiceManager serviceManager) {
        return new MessageLikeRepository(serviceManager);
    }
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
