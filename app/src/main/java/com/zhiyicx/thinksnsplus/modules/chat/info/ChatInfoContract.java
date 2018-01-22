package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

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
    }

    interface Presenter extends IBasePresenter{
        boolean isGroupOwner();
        void updateGroup(String im_group_id, String groupName, String groupIntro, int isPublic,
                         int maxUser, boolean isMemberOnly,  int isAllowInvites, String groupFace);
    }

    interface Repository extends IBaseFriendsRepository{
        Observable<ChatGroupBean> updateGroup(String im_group_id, String groupName, String groupIntro, int isPublic,
                                              int maxUser, boolean isMemberOnly,  int isAllowInvites, String groupFace);
    }
}
