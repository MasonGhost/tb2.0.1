package com.zhiyicx.thinksnsplus.modules.tb.invitation;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }

}
