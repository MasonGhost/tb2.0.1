package com.zhiyicx.thinksnsplus.modules.information.infosearch;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.InfoSearchRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class InfoSearchPresenterMudule {

    SearchContract.View mView;

    public InfoSearchPresenterMudule(SearchContract.View view) {
        mView = view;
    }

    @Provides
    SearchContract.View provideSearchView() {
        return mView;
    }

    @Provides
    SearchContract.Repository provideSearchRepository(ServiceManager serviceManager) {
        return new InfoSearchRepository(serviceManager);
    }
}
