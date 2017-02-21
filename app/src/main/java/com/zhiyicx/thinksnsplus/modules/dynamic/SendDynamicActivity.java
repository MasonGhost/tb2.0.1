package com.zhiyicx.thinksnsplus.modules.dynamic;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class SendDynamicActivity extends TSActivity<SendDynamicPresenter, SendDynamicFragment> {


    @Override
    protected SendDynamicFragment getFragment() {
        return SendDynamicFragment.initFragment(null);
    }

    @Override
    protected void componentInject() {
        DaggerSendDynamicPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .sendDynamicPresenterModule(new SendDynamicPresenterModule(mContanierFragment))
                .build();
    }
}
