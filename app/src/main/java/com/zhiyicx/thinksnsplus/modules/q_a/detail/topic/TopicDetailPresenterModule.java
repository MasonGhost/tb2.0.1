package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */
@Module
public class TopicDetailPresenterModule {

    private TopicDetailContract.View mView;

    public TopicDetailPresenterModule(TopicDetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public TopicDetailContract.View provideTopicDetailContractView(){
        return mView;
    }

}
