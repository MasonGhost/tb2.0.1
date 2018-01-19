package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }
}
