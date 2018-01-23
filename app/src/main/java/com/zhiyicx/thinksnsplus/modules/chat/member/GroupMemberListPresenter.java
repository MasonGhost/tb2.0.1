package com.zhiyicx.thinksnsplus.modules.chat.member;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class GroupMemberListPresenter extends AppBasePresenter<GroupMemberListContract.Repository, GroupMemberListContract.View>
        implements GroupMemberListContract.Presenter{

    @Inject
    public GroupMemberListPresenter(GroupMemberListContract.Repository repository, GroupMemberListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public boolean isOwner() {
        return mRootView.getGroupData().getOwner() == AppApplication.getMyUserIdWithdefault();
    }
}
