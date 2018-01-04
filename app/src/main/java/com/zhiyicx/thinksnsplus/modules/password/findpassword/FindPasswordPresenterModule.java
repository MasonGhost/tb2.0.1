package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class FindPasswordPresenterModule {
    private final FindPasswordContract.View mView;

    public FindPasswordPresenterModule(FindPasswordContract.View view) {
        mView = view;
    }

    @Provides
    FindPasswordContract.View provideFindPasswordContractView() {
        return mView;
    }


}
