package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */

public interface MyFriendsListContract {

    interface View extends ITSListView<UserInfoBean, Presenter> {

    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {
        List<ChatUserInfoBean> getChatUserList(UserInfoBean userInfoBean);
    }

}
