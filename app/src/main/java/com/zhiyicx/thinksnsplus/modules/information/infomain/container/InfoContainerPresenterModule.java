package com.zhiyicx.thinksnsplus.modules.information.infomain.container;

import com.zhiyicx.thinksnsplus.data.source.repository.InfoMainRepository;
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
public class InfoContainerPresenterModule {

    InfoMainContract.InfoContainerView mContainerView;

    public InfoContainerPresenterModule(InfoMainContract.InfoContainerView infoContainerView) {
        mContainerView = infoContainerView;
    }

    @Provides
    InfoMainContract.InfoContainerView provideInfoContainerView() {
        return mContainerView;
    }

    @Provides
    InfoMainContract.Repository provideInfoMainRepository(InfoMainRepository infoMainRepository) {
        return infoMainRepository;
    }

}
