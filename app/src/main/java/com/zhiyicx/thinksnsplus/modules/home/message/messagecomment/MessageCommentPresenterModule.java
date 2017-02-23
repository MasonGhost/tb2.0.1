<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageCommentRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
@Module
public class MessageCommentPresenterModule {
    private final MessageCommentContract.View mView;

    public MessageCommentPresenterModule(MessageCommentContract.View view) {
        mView = view;
    }

    @Provides
    MessageCommentContract.View provideMessageCommentContractView() {
        return mView;
    }

    @Provides
    MessageCommentContract.Repository provideMessageCommentContractRepository(ServiceManager serviceManager) {
        return new MessageCommentRepository(serviceManager);
    }
}
=======
package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageCommentRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
@Module
public class MessageCommentPresenterModule {
    private final MessageCommentContract.View mView;

    public MessageCommentPresenterModule(MessageCommentContract.View view) {
        mView = view;
    }

    @Provides
    MessageCommentContract.View provideMessageCommentContractView() {
        return mView;
    }

    @Provides
    MessageCommentContract.Repository provideMessageCommentContractRepository(ServiceManager serviceManager) {
        return new MessageCommentRepository(serviceManager);
    }
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
