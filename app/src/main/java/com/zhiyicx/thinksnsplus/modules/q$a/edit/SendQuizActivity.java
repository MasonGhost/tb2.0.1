package com.zhiyicx.thinksnsplus.modules.q$a.edit;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 发布问题的页面
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public class SendQuizActivity extends TSActivity<SendQuizPresenter, SendQuizFragment>{
    @Override
    protected SendQuizFragment getFragment() {
        return new SendQuizFragment();
    }

    @Override
    protected void componentInject() {
        DaggerSendQuizComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .sendQuizPresenterModule(new SendQuizPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
