package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public interface FollowFansListContract {
    interface View extends ITSListView<UserInfoBean, Presenter> {
        /**
         * 更新列表的关注状态
         *
         * @param index 需要更新的item位置
         */
        void upDateFollowFansState(int index);

        void upDateFollowFansState();

        /**
         * 获取页面类型
         */
        int getPageType();
    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {
        /**
         * 重写获取网络数据的方法，添加方法参数
         *
         * @param maxId
         * @param isLoadMore
         * @param userId     用户id
         * @param pageType   详见FollowFansListFragment.class定义的页面类型
         */
        void requestNetData(Long maxId, boolean isLoadMore, long userId, int pageType);

        List<UserInfoBean> requestCacheData(Long maxId, boolean isLoadMore, long userId, int pageType);

        /**
         * 关注用户
         *
         * @param index          item所在的列表位置
         * @param followFansBean 被关注的用户id
         */
        void followUser(int index, UserInfoBean followFansBean);

        void cancleFollowUser(int index, UserInfoBean followFansBean);

        /**
         * 清除新 fans 数量
         */
        void cleanNewFans();
    }

}
