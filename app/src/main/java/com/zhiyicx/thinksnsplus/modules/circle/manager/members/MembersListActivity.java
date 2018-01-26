package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class MembersListActivity extends TSActivity<MembersPresenter, MemberListFragment> {

    @Override
    protected MemberListFragment getFragment() {
        return MemberListFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerMembersComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .membersPresenterModule(new MembersPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }
}
