package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class LocationSearchPresenterModule {
    LocationSearchContract.View mView;

    public LocationSearchPresenterModule(LocationSearchContract.View view) {
        mView = view;
    }

    @Provides
    LocationSearchContract.View providesLocationSearchContracttView() {
        return mView;
    }


}
