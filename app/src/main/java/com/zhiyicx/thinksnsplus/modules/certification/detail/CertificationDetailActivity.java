package com.zhiyicx.thinksnsplus.modules.certification.detail;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public class CertificationDetailActivity extends TSActivity<CertificationDetailPresenter, CertificationDetailFragment>{
    @Override
    protected CertificationDetailFragment getFragment() {
        return new CertificationDetailFragment();
    }

    @Override
    protected void componentInject() {
        DaggerCertificationDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .certificationDetailPresenterModule(new CertificationDetailPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
