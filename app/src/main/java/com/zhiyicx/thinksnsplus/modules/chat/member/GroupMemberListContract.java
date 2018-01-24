package com.zhiyicx.thinksnsplus.modules.chat.member;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public interface GroupMemberListContract {
    interface View extends IBaseView<Presenter> {
        ChatGroupBean getGroupData();
        void updateGroup(ChatGroupBean chatGroupBean);
    }

    interface Presenter extends IBasePresenter {
        boolean isOwner();
    }

    interface Repository {
    }
}
