package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppComponent;

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
}
