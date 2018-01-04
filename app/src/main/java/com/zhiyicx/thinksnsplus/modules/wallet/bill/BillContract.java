package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/06/02/15:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface BillContract {
    interface View extends ITSListView<RechargeSuccessBean, Presenter> {
        HeaderAndFooterWrapper getTSAdapter();
        int getBillType();
    }

    interface Presenter extends ITSListPresenter<RechargeSuccessBean> {
        void selectBillByAction(int action);

        void selectAll();


    }

}
