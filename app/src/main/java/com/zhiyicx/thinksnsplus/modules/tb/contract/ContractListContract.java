package com.zhiyicx.thinksnsplus.modules.tb.contract;


import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;

public interface ContractListContract {
    interface View extends ITSListView<ContractData,Presenter> {
        String getType();
    }
    interface Presenter extends ITSListPresenter<ContractData> {}
}
