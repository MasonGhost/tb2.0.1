package com.zhiyicx.thinksnsplus.modules.information.my_info;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/08/28/14:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class ManuscripListPresenterModule {

    ManuscriptListContract.View mView;

    public ManuscripListPresenterModule(ManuscriptListContract.View view) {
        mView = view;
    }

    @Provides
    ManuscriptListContract.View provideManuscriptContractView(){
        return mView;
    }

}
