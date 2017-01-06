package com.zhiyicx.thinksnsplus.modules.chat;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.register.DaggerRegisterComponent;
import com.zhiyicx.thinksnsplus.modules.register.RegisterContract;
import com.zhiyicx.thinksnsplus.modules.register.RegisterFragment;
import com.zhiyicx.thinksnsplus.modules.register.RegisterPresenterModule;

public class ChatActivity extends TSActivity {


    @Override
    protected void componentInject() {
        DaggerRegisterComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .registerPresenterModule(new RegisterPresenterModule((RegisterContract.View) mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected Fragment getFragment() {
        return RegisterFragment.newInstance();
    }

}
