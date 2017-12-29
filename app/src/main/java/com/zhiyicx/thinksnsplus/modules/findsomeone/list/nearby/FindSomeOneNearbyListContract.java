package com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.NearbyBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */

public interface FindSomeOneNearbyListContract {
    interface View extends ITSListView<NearbyBean, Presenter> {
        /**
         * 更新列表的关注状态
         *
         * @param index 需要更新的item位置
         */
        void upDateFollowFansState(int index);

        void upDateFollowFansState();

    }

    interface Presenter extends ITSListPresenter<NearbyBean> {
        /**
         * 重写获取网络数据的方法，添加方法参数
         *
         * @param page
         * @param isLoadMore
         */
        void requestNetData(Long page, boolean isLoadMore);

        /**
         * 关注用户
         *
         * @param index          item所在的列表位置
         * @param followFansBean 被关注的用户id
         */
        void followUser(int index, UserInfoBean followFansBean);

        void cancleFollowUser(int index, UserInfoBean followFansBean);

    }


}
