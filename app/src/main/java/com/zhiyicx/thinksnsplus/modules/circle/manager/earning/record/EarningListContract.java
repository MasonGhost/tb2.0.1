package com.zhiyicx.thinksnsplus.modules.circle.manager.earning.record;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/06/02/15:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface EarningListContract {
    interface View extends ITSListView<CircleEarningListBean, Presenter> {
        HeaderAndFooterWrapper getTSAdapter();

        long getCircleId();

        Long getStartTime();

        Long getEndTime();

        String getType();
    }

    interface Presenter extends ITSListPresenter<CircleEarningListBean> {
        void selectBillByAction(int action);
        void selectAll();
    }
}
