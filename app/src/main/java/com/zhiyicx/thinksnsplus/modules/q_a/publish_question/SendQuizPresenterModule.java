package com.zhiyicx.thinksnsplus.modules.q_a.publish_question;

import com.zhiyicx.thinksnsplus.data.source.repository.SendQuizRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */
@Module
public class SendQuizPresenterModule {

    private SendQuizContract.View mView;

    public SendQuizPresenterModule(SendQuizContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SendQuizContract.View provideSendQuizContractView(){
        return mView;
    }

    @Provides
    public SendQuizContract.Repository provideSendQuizContractRepository(SendQuizRepository repository){
        return repository;
    }
}
