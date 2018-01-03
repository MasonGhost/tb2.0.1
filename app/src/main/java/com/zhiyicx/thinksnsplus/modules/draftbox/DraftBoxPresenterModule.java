package com.zhiyicx.thinksnsplus.modules.draftbox;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/08/22/13:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class DraftBoxPresenterModule {
    DraftBoxContract.View mView;

    public DraftBoxPresenterModule(DraftBoxContract.View view) {
        mView = view;
    }

    @Provides
    DraftBoxContract.View providesDraftBoxContractView() {
        return mView;
    }

}
