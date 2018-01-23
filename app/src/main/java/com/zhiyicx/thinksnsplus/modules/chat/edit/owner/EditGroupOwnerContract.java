package com.zhiyicx.thinksnsplus.modules.chat.edit.owner;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

import java.util.List;

/**
 * Created by Catherine on 2018/1/22.
 */

public interface EditGroupOwnerContract {

    interface View extends ITSListView<UserInfoBean, Presenter> {
        ChatGroupBean getGroupData();
        void updateGroup(ChatGroupBean chatGroupBean);
    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {
        boolean checkNewOwner(UserInfoBean userInfoBean);
        List<UserInfoBean> getSearchResult(String key);
        void updateGroup(ChatGroupBean chatGroupBean);
    }

    interface Repository extends IBaseFriendsRepository{
    }
}
