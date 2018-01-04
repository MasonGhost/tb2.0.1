package com.zhiyicx.thinksnsplus.modules.information.infosearch;

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

}
