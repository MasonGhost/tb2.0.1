package com.zhiyicx.thinksnsplus.modules.tb.privacy;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/03/01/15:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class PrivacyPresenterModule {
    PrivacyContract.View mView;

    public PrivacyPresenterModule(PrivacyContract.View view) {
        mView = view;
    }

    @Provides
    PrivacyContract.View providePrivacyView() {
        return mView;
    }
}
