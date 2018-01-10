package com.zhiyicx.thinksnsplus.modules.chat.location;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 发送定位和查看定位详情的页面
 * @date 2018/1/10
 * @contact email:648129313@qq.com
 */

public class SendLocationActivity extends TSActivity<SendLocationPresenter, SendLocationFragment>{

    @Override
    protected SendLocationFragment getFragment() {
        return new SendLocationFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerSendLocationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .sendLocationPresenterModule(new SendLocationPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
