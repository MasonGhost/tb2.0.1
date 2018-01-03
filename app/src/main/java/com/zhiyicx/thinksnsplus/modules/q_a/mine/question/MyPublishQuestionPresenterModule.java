package com.zhiyicx.thinksnsplus.modules.q_a.mine.question;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */
@Module
public class MyPublishQuestionPresenterModule {
    private MyPublishQuestionContract.View mView;

    public MyPublishQuestionPresenterModule(MyPublishQuestionContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public MyPublishQuestionContract.View provideMyPublishQuestionContractView() {
        return mView;
    }

}
