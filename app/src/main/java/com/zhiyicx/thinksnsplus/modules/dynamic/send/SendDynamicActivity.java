package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;


public class SendDynamicActivity extends TSActivity<SendDynamicPresenter, SendDynamicFragment> {
    public static final String DYNAMIC_TYPE = "dynamic_type";// 动态类型
    public static final int PHOTO_TEXT_DYNAMIC = 0;// 图片文字动态
    public static final int TEXT_ONLY_DYNAMIC = 1;// 纯文字动态


    @Override
    protected SendDynamicFragment getFragment() {
        return SendDynamicFragment.initFragment(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerSendDynamicPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .sendDynamicPresenterModule(new SendDynamicPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
