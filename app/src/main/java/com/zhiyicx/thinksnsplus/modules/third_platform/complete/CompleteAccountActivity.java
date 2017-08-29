package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 第三方注册完善资料
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class CompleteAccountActivity extends TSActivity<CompleteAccountPresenter, CompleteAccountFragment>{

    @Override
    protected CompleteAccountFragment getFragment() {
        return new CompleteAccountFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerCompleteAccountComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .completeAccountPresenterModule(new CompleteAccountPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
