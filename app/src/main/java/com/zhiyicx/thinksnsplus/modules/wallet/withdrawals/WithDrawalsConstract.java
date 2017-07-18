package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
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
        void minMoneyLimit();
        void maxMoneyLimit();
        void configSureBtn(boolean enable);
        void initWithdrawalsInstructionsPop(int resId);
        WalletConfigBean getWalletConfigBean();
        double getMoney();
    }

    interface Presenter extends IBasePresenter{
        void withdraw(double value,String type,String account);
    }

    interface Repository{
        Observable<WithdrawResultBean> withdraw(double value,String type,String account);
    }
}
