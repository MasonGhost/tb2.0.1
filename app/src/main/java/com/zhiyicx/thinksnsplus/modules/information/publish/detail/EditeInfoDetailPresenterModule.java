package com.zhiyicx.thinksnsplus.modules.information.publish.detail;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/01/24/11:12
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class EditeInfoDetailPresenterModule {
    EditeInfoDetailContract.View mView;

    public EditeInfoDetailPresenterModule(EditeInfoDetailContract.View view) {
        mView = view;
    }

    @Provides
    EditeInfoDetailContract.View provideEditeInfoDetailView(){
        return mView;
    }

}
