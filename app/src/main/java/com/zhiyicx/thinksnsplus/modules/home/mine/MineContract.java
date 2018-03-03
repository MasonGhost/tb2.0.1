package com.zhiyicx.thinksnsplus.modules.home.mine;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskRewardRuleBean;

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
         * 新的关注
         * @param count
         */
        void setNewFollowTip(int count);

        /**
         * 是否有新系统消息
         */
        void setNewSystemInfo(boolean isShow);

        void updateCertification(UserCertificationInfo info);

        void  checkinSucces(CheckInBean data);

        void getCheckInInfoSuccess(CheckInBean data);

        void getTaskInfoSuccess(TBTaskContainerBean data);

        void getTaskRewardRuleSuccess(TBTaskRewardRuleBean data);
    }

    interface Presenter extends IBaseTouristPresenter {
        /**
         * 从数据库获取当前用户的信息
         */
        void getUserInfoFromDB();
        /**
         * 通过 key 标记消息已读
         *
         * @param key
         */
        void readMessageByKey(String key);

        /**
         * 更新用户信息
         */
        void updateUserInfo();

        int getBalanceRatio();

        void getCertificationInfo();

        void checkIn();

        void getCheckInfo();

        void getTaskInfo();

        void getTaskRewardRule();

    }

}
