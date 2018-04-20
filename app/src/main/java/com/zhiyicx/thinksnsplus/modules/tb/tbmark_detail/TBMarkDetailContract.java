package com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CandyWalletOrderBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.tbcandy.CandyCateBean;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */

public interface TBMarkDetailContract {

    interface View extends ITSListView<CandyWalletOrderBean,TBMarkDetailContract.Presenter> {
        void updateUserInfo(UserInfoBean data);
        CandyCateBean getCurrentCandy();
    }
    interface Presenter extends ITSListPresenter<CandyWalletOrderBean> {
        void getUserInfo();
    }
}
