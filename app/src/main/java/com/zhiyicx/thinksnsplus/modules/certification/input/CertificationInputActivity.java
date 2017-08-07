package com.zhiyicx.thinksnsplus.modules.certification.input;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */

public class CertificationInputActivity extends TSActivity<CertificationInputPresenter, CertificationInputFragment>{

    public static final String BUNDLE_CERTIFICATION_TYPE = "bundle_certification_type";
    public static final String BUNDLE_TYPE = "bundle_type";

    @Override
    protected CertificationInputFragment getFragment() {
        return new CertificationInputFragment().instance(getIntent().getBundleExtra(BUNDLE_CERTIFICATION_TYPE));
    }

    @Override
    protected void componentInject() {
        DaggerCertificationInputComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .certificationInputPresenterModule(new CertificationInputPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
