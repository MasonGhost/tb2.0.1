package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/05/24/9:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface WithdrawalsDetailConstract {
    interface View extends ITSListView<WithdrawalsListBean,Presenter>{

    }

    interface Presenter extends ITSListPresenter<WithdrawalsListBean>{}

    interface Repository{
        /**
         *
         * @param after 获取更多数据，上一次获取列表的最后一条 ID
         * @return
         */
        Observable<List<WithdrawalsListBean>> getWithdrawListDetail(int after);
    }
}
