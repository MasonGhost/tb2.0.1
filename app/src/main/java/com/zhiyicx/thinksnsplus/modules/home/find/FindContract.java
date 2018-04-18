package com.zhiyicx.thinksnsplus.modules.home.find;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;

/**
 * Created by Administrator on 2018/4/18.
 */

public interface FindContract {
    interface View extends ITSListView<RechargeSuccessBean, Presenter> {

    }

    interface Presenter extends ITSListPresenter<RechargeSuccessBean> {

    }
}
