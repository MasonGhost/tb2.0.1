package com.zhiyicx.thinksnsplus.modules.home.mine;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/9
 * @contact email:450127106@qq.com
 */

public interface MineContract {
    interface View extends IBaseView<MineContract.Presenter> {
        /**
         * 设置当前用户的用户信息
         *
         * @param userInfoBean
         */
        void setUserInfo(UserInfoBean userInfoBean);

        /**
         * 设置关注数量
         */
        void updateUserFollowCount(int stateFollow);
    }

    interface Presenter extends IBasePresenter {
        /**
         * 从数据库获取当前用户的信息
         */
        void getUserInfoFromDB();

    }

    interface Repository {

    }
}
