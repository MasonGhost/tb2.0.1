package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @Author Jliuer
 * @Date 2018/02/28/19:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface WalletContract {
    interface View extends ITSListView<WalletData,Presenter>{
        void updateUserInfo(UserInfoBean data);
    }
    interface Presenter extends ITSListPresenter<WalletData>{
        void getUserInfo();
    }
}
