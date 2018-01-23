package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

/**
 * @Author Jliuer
 * @Date 2017/06/02/15:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface IntegrationDetailContract {
    interface View extends ITSListView<RechargeSuccessBean, Presenter> {
        HeaderAndFooterWrapper getTSAdapter();
        Integer getBillType();

        void setMaxId(long maxId);
    }

    interface Presenter extends ITSListPresenter<RechargeSuccessBean> {
        void selectBillByAction(int action);

        void selectAll();


    }

}
