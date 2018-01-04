package com.zhiyicx.thinksnsplus.modules.system_conversation;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.ISystemRepository;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public interface SystemConversationContract {

    interface View extends ITSListView<SystemConversationBean, Presenter> {

    }

    interface Presenter extends ITSListPresenter<SystemConversationBean> {

    }
}
