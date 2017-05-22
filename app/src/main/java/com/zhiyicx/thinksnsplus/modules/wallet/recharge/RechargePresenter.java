package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class RechargePresenter extends AppBasePresenter<RechargeContract.Repository, RechargeContract.View> implements RechargeContract.Presenter {

    @Inject
    AuthRepository mIAuthRepository;

    @Inject
    public RechargePresenter(RechargeContract.Repository repository, RechargeContract.View rootView) {
        super(repository, rootView);
    }

}
