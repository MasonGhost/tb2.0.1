package com.zhiyicx.thinksnsplus.modules.circle.publish;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/01/23/16:13
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class PublishPostPresenterModule {

    PublishPostContract.View mView;

    public PublishPostPresenterModule(PublishPostContract.View view) {
        mView = view;
    }

    @Provides
    PublishPostContract.View providePublishPostContractView(){
        return mView;
    }
}
