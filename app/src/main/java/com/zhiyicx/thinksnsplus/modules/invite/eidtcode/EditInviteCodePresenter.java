package com.zhiyicx.thinksnsplus.modules.invite.eidtcode;

import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class EditInviteCodePresenter extends BasePresenter<EditInviteCodeContract.View> implements EditInviteCodeContract.Presenter {



    @Inject
    public EditInviteCodePresenter(EditInviteCodeContract.View rootView) {
        super(rootView);
    }


    @Override
    public void submitInviteCode(String inviteCode) {

    }
}
