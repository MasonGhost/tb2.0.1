package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import com.zhiyicx.thinksnsplus.data.source.repository.MessageReviewRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/7/5/21:12
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class MessageReviewPresenterModule {
    private final MessageReviewContract.View mView;

    public MessageReviewPresenterModule(MessageReviewContract.View view) {
        mView = view;
    }

    @Provides
    MessageReviewContract.View provideMessageLikeContractView() {
        return mView;
    }


    @Provides
    MessageReviewContract.Repository provideMessageLikeContractRepository(MessageReviewRepository
                                                                                  messageReviewRepository) {
        return messageReviewRepository;
    }
}
