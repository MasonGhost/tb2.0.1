package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/05/23/14:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithDrawalsPresenter extends AppBasePresenter<WithDrawalsConstract.Repository,WithDrawalsConstract.View>
        implements WithDrawalsConstract.Presenter {

    @Inject
    public WithDrawalsPresenter(WithDrawalsConstract.Repository repository, WithDrawalsConstract.View rootView) {
        super(repository, rootView);
    }
}
