package com.zhiyicx.thinksnsplus.modules.dynamic;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;

public class SendDynamicActivity extends TSActivity<SendDynamicPresenter, SendDynamicFragment> {


    @Override
    protected SendDynamicFragment getFragment() {
        return null;
    }

    @Override
    protected void componentInject() {

    }
}
