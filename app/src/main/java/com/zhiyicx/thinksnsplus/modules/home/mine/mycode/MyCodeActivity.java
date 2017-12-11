package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 我的二维码页面
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public class MyCodeActivity extends TSActivity<MyCodePresenter, MyCodeFragment>{
    @Override
    protected MyCodeFragment getFragment() {
        return new MyCodeFragment();
    }

    @Override
    protected void componentInject() {
        DaggerMyCodeComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myCodePresenterModule(new MyCodePresenterModule(mContanierFragment))
                .shareModule(new ShareModule(MyCodeActivity.this))
                .build()
                .inject(this);
    }
}
