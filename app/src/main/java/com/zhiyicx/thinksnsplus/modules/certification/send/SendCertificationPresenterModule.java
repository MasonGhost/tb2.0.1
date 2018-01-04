package com.zhiyicx.thinksnsplus.modules.certification.send;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */
@Module
public class SendCertificationPresenterModule {

    private SendCertificationContract.View mView;

    public SendCertificationPresenterModule(SendCertificationContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SendCertificationContract.View provideSendCertificationContractView(){
        return mView;
    }

}
