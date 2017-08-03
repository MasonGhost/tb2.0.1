package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind.ChooseBindActivity.BUNDLE_THIRD_INFO;

/**
 * @author Catherine
 * @describe 第三方注册完善资料
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class CompleteAccountActivity extends TSActivity<CompleteAccountPresenter, CompleteAccountFragment>{

    @Override
    protected CompleteAccountFragment getFragment() {
        return new CompleteAccountFragment().instance(getIntent().getBundleExtra(BUNDLE_THIRD_INFO));
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
