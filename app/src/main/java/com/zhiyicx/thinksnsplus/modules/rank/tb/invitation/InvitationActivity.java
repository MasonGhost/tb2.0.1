package com.zhiyicx.thinksnsplus.modules.rank.tb.invitation;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:44
 * @Email Jliuer@aliyun.com
 * @Description
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
