package com.zhiyicx.thinksnsplus.modules.certification.send;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 选择证件 上传认证申请
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public class SendCertificationActivity extends TSActivity<SendCertificationPresenter, SendCertificationFragment>{

    public static final String BUNDLE_SEND_CERTIFICATION = "bundle_send_certification";

    @Override
    protected SendCertificationFragment getFragment() {
        return new SendCertificationFragment().instance(getIntent().getBundleExtra(BUNDLE_SEND_CERTIFICATION));
    }

    @Override
    protected void componentInject() {
        DaggerSendCertificationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .sendCertificationPresenterModule(new SendCertificationPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
