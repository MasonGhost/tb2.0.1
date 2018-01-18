package com.zhiyicx.thinksnsplus.modules.home.mine.scan;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 扫码的页面哦
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public class ScanCodeActivity extends TSActivity<ScanCodePresenter, ScanCodeFragment>{

    @Override
    protected ScanCodeFragment getFragment() {
        return new ScanCodeFragment();
    }

    @Override
    protected void componentInject() {
        DaggerScanCodeComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .scanCodePresenterModule(new ScanCodePresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
