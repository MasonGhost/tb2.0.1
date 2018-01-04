package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind.ChooseBindActivity.BUNDLE_THIRD_INFO;

/**
 * @author Catherine
 * @describe 绑定已有的账号
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class BindOldAccountActivity extends TSActivity<BindOldAccountPresenter, BindOldAccountFragment>{
    @Override
    protected BindOldAccountFragment getFragment() {
        return  BindOldAccountFragment.instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerBindOldAccountComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .bindOldAccountPresenterModule(new BindOldAccountPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
