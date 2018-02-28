package com.zhiyicx.thinksnsplus.modules.wallet.tb;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;

/**
 * @Author Jliuer
 * @Date 2018/02/28/19:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface WalletContract {
    interface View extends ITSListView<WalletData,Presenter>{}
    interface Presenter extends ITSListPresenter<WalletData>{}
}
