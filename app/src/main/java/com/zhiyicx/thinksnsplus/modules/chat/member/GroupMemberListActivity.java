package com.zhiyicx.thinksnsplus.modules.chat.member;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class GroupMemberListActivity extends TSActivity<GroupMemberListPresenter, GroupMemberListFragment>{

    @Override
    protected GroupMemberListFragment getFragment() {
        return new GroupMemberListFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerGroupMemberListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .groupMemberListPresenterModule(new GroupMemberListPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
