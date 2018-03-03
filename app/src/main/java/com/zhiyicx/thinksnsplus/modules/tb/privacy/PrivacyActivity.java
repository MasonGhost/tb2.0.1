package com.zhiyicx.thinksnsplus.modules.tb.privacy;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author Jliuer
 * @Date 2018/02/28/18:36
 * @Email Jliuer@aliyun.com
 * @Description 隐私管理
 */
public class PrivacyActivity extends TSActivity<PrivacyPresenter, PrivacyFragment> {

    @Override
    protected PrivacyFragment getFragment() {
        return new PrivacyFragment();
    }

    @Override
    protected void componentInject() {
        DaggerPrivacyComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .privacyPresenterModule(new PrivacyPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
