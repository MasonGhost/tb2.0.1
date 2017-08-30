package com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 选择是否绑定已有账号或者注册新账号的页面
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class ChooseBindActivity extends TSActivity<ChooseBindPresenter, ChooseBindFragment>{

    public static final String BUNDLE_THIRD_INFO = "bundle_third_info";

    @Override
    protected ChooseBindFragment getFragment() {
        return new ChooseBindFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerChooseBindComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .chooseBindPresenterModule(new ChooseBindPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }
}
