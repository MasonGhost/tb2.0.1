package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import com.zhiyicx.thinksnsplus.data.source.repository.InfoPublishAddInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.InfoPublishRepository;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoContract;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class AddInfoPresenterModule {
    AddInfoContract.View mView;

    public AddInfoPresenterModule(AddInfoContract.View view) {
        mView = view;
    }

    @Provides
    AddInfoContract.View providesAddInfoContractView(){
        return mView;
    }

    @Provides
    AddInfoContract.Repository providesAddInfoContractRepository(InfoPublishAddInfoRepository repository){
        return repository;
    }
}
