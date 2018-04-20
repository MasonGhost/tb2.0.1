package com.zhiyicx.thinksnsplus.modules.tb.exchange;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.tbcandy.CandyBean;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */

public interface ExchangeContract {

    interface View extends IBaseView<Presenter> {
        CandyBean getCurrentCandy();
        void getCandySuccess(CandyBean candyBean);
    }
    interface Presenter extends IBaseTouristPresenter {
        void getCandy();
        void orderCandy(int tbmark, int candy_id);
    }
}
