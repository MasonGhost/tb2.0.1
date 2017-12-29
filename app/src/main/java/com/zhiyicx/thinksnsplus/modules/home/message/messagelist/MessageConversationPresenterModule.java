package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import com.zhiyicx.thinksnsplus.data.source.repository.MessageConversationRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */
@Module
public class MessageConversationPresenterModule {

    private MessageConversationContract.View mView;

    public MessageConversationPresenterModule(MessageConversationContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public MessageConversationContract.View provideMessageConversationContractView(){
        return mView;
    }

    @Provides
    public MessageConversationContract.Repository provedeMessageConversationContractRepository(MessageConversationRepository repository){
        return repository;
    }
}
