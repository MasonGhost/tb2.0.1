package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class PersonalCenterActivity extends TSActivity<PersonalCenterPresenter, PersonalCenterFragment> {

    @Override
    protected void componentInject() {
        DaggerPersonalCenterPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .personalCenterPresenterModule(new PersonalCenterPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected PersonalCenterFragment getFragment() {
        return PersonalCenterFragment.initFragment(getIntent().getExtras());
    }
}
