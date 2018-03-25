package com.zhiyicx.thinksnsplus.modules.tb.message;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBean;

/**
 * Created by lx on 2018/3/24.
 */

public interface MessageListContract {
    interface View extends ITSListView<TbMessageBean,MessageListContract.Presenter> {
    }
    interface Presenter extends ITSListPresenter<TbMessageBean> {
        void updateMessageReadStaus(TbMessageBean tbMessageBean);
    }
}
