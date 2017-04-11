package com.zhiyicx.thinksnsplus.modules.rank;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DigBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */

public interface RankContract {
    interface View extends ITSListView<DigBean, Presenter> {
        /**
         * 更新列表的关注状态
         *
         * @param index 需要更新的item位置
         */
        void upDateFollowFansState(int index);

        void upDateFollowFansState();

    }

    interface Presenter extends ITSListPresenter<DigBean> {


    }

    interface Repository extends UserInfoContract.Repository{


    }
}
