package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class PersonalCenterActivity extends TSActivity<PersonalCenterPresenter, PersonalCenterFragment> {

    @Override
    protected void componentInject() {
        DaggerPersonalCenterPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .shareModule(new ShareModule(this))
                .personalCenterPresenterModule(new PersonalCenterPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected PersonalCenterFragment getFragment() {
        return PersonalCenterFragment.initFragment(getIntent().getExtras());
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }
}
