package com.zhiyicx.thinksnsplus.modules.guide;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/13
 * @Contact master.jungle68@gmail.com
 */
public class GuideActivity extends TSActivity<GuidePresenter, GuideFragment_v2> {

    @Override
    protected GuideFragment_v2 getFragment() {
        return new GuideFragment_v2();
    }

    @Override
    protected void componentInject() {
        DaggerGuideComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .guidePresenterModule(new GuidePresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mContanierFragment.onNewIntent(intent);
    }
}
