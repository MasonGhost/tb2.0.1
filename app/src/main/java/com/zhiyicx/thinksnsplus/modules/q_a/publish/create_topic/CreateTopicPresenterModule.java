package com.zhiyicx.thinksnsplus.modules.q_a.publish.create_topic;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/09/15/10:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class CreateTopicPresenterModule {
    CreateTopicContract.View mView;

    public CreateTopicPresenterModule(CreateTopicContract.View view) {
        mView = view;
    }

    @Provides
    CreateTopicContract.View providesCreateTopicContractView() {
        return mView;
    }

}
