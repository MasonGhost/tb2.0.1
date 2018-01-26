package com.zhiyicx.thinksnsplus.modules.chat.location;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/10
 * @contact email:648129313@qq.com
 */
@Module
public class SendLocationPresenterModule {

    private SendLocationContract.View mView;

    public SendLocationPresenterModule(SendLocationContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SendLocationContract.View provideSendLocationContractView() {
        return mView;
    }
}
