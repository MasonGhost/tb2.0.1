package com.zhiyicx.thinksnsplus.modules.certification.input;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */
@Module
public class CertificationInputPresenterModule {

    private CertificationInputContract.View mView;

    public CertificationInputPresenterModule(CertificationInputContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public CertificationInputContract.View provideCertificationInputContractView(){
        return mView;
    }

}
