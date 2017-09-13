package com.zhiyicx.thinksnsplus.modules.personal_center.portrait;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 查看头像大图以及一些操作处理的页面，至于是什么操作，我也不知道嘻嘻
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */

public class HeadPortraitViewActivity extends TSActivity<HeadPortraitViewPresenter, HeadPortraitViewFragment>{

    public static final String BUNDLE_USER_INFO = "user_info";

    @Override
    protected HeadPortraitViewFragment getFragment() {
        return HeadPortraitViewFragment.instance(getIntent().getBundleExtra(BUNDLE_USER_INFO));
    }

    @Override
    protected void componentInject() {
        DaggerHeadPortraitViewComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .headPortraitViewPresenterModule(new HeadPortraitViewPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
