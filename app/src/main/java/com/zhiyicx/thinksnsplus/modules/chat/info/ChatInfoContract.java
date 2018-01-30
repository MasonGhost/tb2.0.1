package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

import java.util.List;

import retrofit2.http.Query;
import rx.Observable;


/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public interface ChatInfoContract {

    interface View extends IBaseView<Presenter> {
        String getChatId();

        void updateGroup(ChatGroupBean chatGroupBean);

        void getGroupInfoSuccess(ChatGroupBean chatGroupBean);

        ChatGroupBean getGroupBean();

        void isShowEmptyView(boolean isShow, boolean isSuccess);

        /**
         * 单聊的对方的id
         */
        String getToUserId();

        void createGroupSuccess(ChatGroupBean chatGroupBean);

        void closeCurrentActivity();
    }

    interface Presenter extends IBasePresenter {
        boolean isGroupOwner();

        void updateGroup(ChatGroupBean chatGroupBean, boolean isEditGroupFace);

        void getGroupChatInfo(String groupId);

        void createGroupFromSingleChat();

        /**
         * 接受，或者屏蔽群消息
         * @param isChecked
         * @param chatId
         */
        void openOrCloseGroupMessage(boolean isChecked, String chatId);

        /**
         * 退群 或者 解散群
         * 群主是 解散，一般成员是退群
         * @param chatId
         */
        void destoryOrLeaveGroup(String chatId);
    }

    interface Repository extends IBaseFriendsRepository {
        Observable<List<ChatGroupBean>> getGroupChatInfo(String groupId);
    }
}
