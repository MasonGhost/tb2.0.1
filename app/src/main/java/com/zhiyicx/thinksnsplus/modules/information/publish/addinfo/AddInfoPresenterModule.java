package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

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

}
