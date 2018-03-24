package com.zhiyicx.thinksnsplus.modules.tb.contract;


import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.HintSideBarUserBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

public interface ContractListContract {
    interface View extends ITSListView<HintSideBarUserBean,Presenter> {
    }
    interface Presenter extends ITSListPresenter<HintSideBarUserBean> {}
}
