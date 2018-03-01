package com.zhiyicx.thinksnsplus.modules.tb.invitation;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.rank.tb.invitation.DaggerInvitationComponent;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:44
 * @Email Jliuer@aliyun.com
 * @Description 邀请好友 二维码
 */
public class InvitationActivity extends TSActivity<InvitationPresenter, InvitationFragment> {

    @Override
    protected InvitationFragment getFragment() {
        return InvitationFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerInvitationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .invitationPresenterModule(new InvitationPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
