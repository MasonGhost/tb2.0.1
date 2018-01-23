package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
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

    interface View extends IBaseView<Presenter>{
        String getChatId();
        void updateGroup(ChatGroupBean chatGroupBean);
        void getGroupInfoSuccess(ChatGroupBean chatGroupBean);
        ChatGroupBean getGroupBean();
        void isShowEmptyView(boolean isShow, boolean isSuccess);
    }

    interface Presenter extends IBasePresenter{
        boolean isGroupOwner();
        void updateGroup(ChatGroupBean chatGroupBean, boolean isEditGroupFace);
        void getGroupChatInfo(String groupId);
    }

    interface Repository extends IBaseFriendsRepository{
        Observable<List<ChatGroupBean>> getGroupChatInfo(String groupId);
    }
}
