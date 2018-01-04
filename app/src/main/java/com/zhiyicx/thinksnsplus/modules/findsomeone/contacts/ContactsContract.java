package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ContactsContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public interface ContactsContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IBaseView<Presenter> {

        void updateContacts(ArrayList<ContactsContainerBean> data);

    }

    interface Presenter extends IBaseTouristPresenter {

        void getContacts();

        /**
         * 关注用户
         *
         * @param index          item所在的列表位置
         * @param followFansBean 被关注的用户id
         */
        void followUser(int index, UserInfoBean followFansBean);

        void cancleFollowUser(int index, UserInfoBean followFansBean);

        /**
         * @return 邀请模板
         */
        String getInviteSMSTip();

    }

}
