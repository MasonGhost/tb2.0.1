package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class InfoListPresenterModule {

    InfoMainContract.InfoListView mInfoListView;

    public InfoListPresenterModule(InfoMainContract.InfoListView infoListView) {
        mInfoListView = infoListView;
    }

    @Provides
    InfoMainContract.InfoListView provideInfoListView() {
        return mInfoListView;
    }


}
