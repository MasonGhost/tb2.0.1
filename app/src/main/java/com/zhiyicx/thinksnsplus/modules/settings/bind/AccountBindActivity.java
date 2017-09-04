package com.zhiyicx.thinksnsplus.modules.settings.bind;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 处理手机和邮箱的绑定和解绑的页面
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountBindActivity extends TSActivity<AccountBindPresenter, AccountBindFragment>{

    public static final String BUNDLE_BIND_TYPE = "bundle_bind_type";
    public static final String BUNDLE_BIND_STATE = "bundle_bind_state";
    public static final String BUNDLE_BIND_DATA= "bundle_bind_data";

    @Override
    protected AccountBindFragment getFragment() {
        return new AccountBindFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerAccountBindComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .accountBindPresenterModule(new AccountBindPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
