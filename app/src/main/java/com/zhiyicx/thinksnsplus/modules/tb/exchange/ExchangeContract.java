package com.zhiyicx.thinksnsplus.modules.tb.exchange;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
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

    interface View extends ITSListView<CandyBean,ExchangeContract.Presenter> {
        void updateUserInfo(UserInfoBean data);
        int getCandyId();
        String getBillType();
    }
    interface Presenter extends ITSListPresenter<CandyBean> {
        void getUserInfo();
    }
}
