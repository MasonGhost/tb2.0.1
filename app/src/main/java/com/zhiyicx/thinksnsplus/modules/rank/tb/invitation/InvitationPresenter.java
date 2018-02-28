package com.zhiyicx.thinksnsplus.modules.rank.tb.invitation;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InvitationPresenter extends AppBasePresenter<InvitationContract.View> implements InvitationContract.Presenter {

    @Inject
    public InvitationPresenter(InvitationContract.View rootView) {
        super(rootView);
    }
}
