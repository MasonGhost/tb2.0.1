package com.zhiyicx.thinksnsplus.modules.information.publish;

import com.zhiyicx.thinksnsplus.data.source.repository.InfoPublishRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class PublishInfoPresenterModule {
    PublishInfoContract.View mView;

    public PublishInfoPresenterModule(PublishInfoContract.View view) {
        mView = view;
    }

    @Provides
    PublishInfoContract.View providesPublishInfoContractView(){
        return mView;
    }

    @Provides
    PublishInfoContract.Repository providesPublishInfoContractRepository(InfoPublishRepository infoPublishRepository){
        return infoPublishRepository;
    }
}
