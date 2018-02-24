package com.zhiyicx.thinksnsplus.modules.chat;

import com.hyphenate.chat.EMMessage;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public interface ChatContract {

    interface View extends IBaseView<Presenter> {
        void onMessageReceivedWithUserInfo(List<EMMessage> messages);
        void setGoupName(String name);

        /**
         * 更新标题
         * @param s
         */
        void setTitle(String s);
    }

    interface Presenter extends IBasePresenter {
        void dealMessages(List<EMMessage> messages);
        String getUserName(String id);

        /**
         * 从本地拿
         * @param id
         * @return
         */
        ChatGroupBean getChatGroupInfo(String id);

        /**
         * 从服务器拿
         * @param groupId
         */
        void getGroupChatInfo(String groupId);
        void updateGroupName(ChatGroupBean chatGroupBean);
        String getGroupName(String id);
        boolean updateChatGroupMemberCount(String id,int count,boolean add);
    }
}
