package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/05/23/14:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface WithDrawalsConstract {

    interface View extends IBaseView<Presenter>{
        void withdrawResult(WithdrawResultBean withdrawResultBean);
    }

    interface Presenter extends IBasePresenter{
        void withdraw(int value,String type,String account);
    }

    interface Repository{
        Observable<WithdrawResultBean> withdraw(int value,String type,String account);
    }
}
