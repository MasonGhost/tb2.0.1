package com.zhiyicx.thinksnsplus.modules.chat.edit.manager;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public interface GroupManagerContract {

    interface View extends IBaseView<Presenter> {
        void closeCurrentActivity();
        void updateGroup(ChatGroupBean chatGroupBean);
    }

    interface Presenter extends IBasePresenter {
        void updateGroup(ChatGroupBean chatGroupBean);
    }

    interface Repository extends IBaseFriendsRepository{

    }
}
