package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

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
}
