package com.zhiyicx.thinksnsplus.modules.certification.detail;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */
@Module
public class CertificationDetailPresenterModule {

    private CertificationDetailContract.View mView;

    public CertificationDetailPresenterModule(CertificationDetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public CertificationDetailContract.View provideCertificationDetailContractView(){
        return mView;
    }

}
