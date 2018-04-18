package com.zhiyicx.thinksnsplus.modules.home.find;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.tbcandy.CandyBean;

/**
 * Created by Administrator on 2018/4/18.
 */

public interface FindContract {
    interface View extends ITSListView<CandyBean, Presenter> {

    }

    interface Presenter extends ITSListPresenter<CandyBean> {

    }
}
