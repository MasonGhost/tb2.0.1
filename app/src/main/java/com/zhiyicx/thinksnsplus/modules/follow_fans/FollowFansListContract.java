package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansItemBean;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public interface FollowFansListContract {
    interface View extends ITSListView<FollowFansBean, Presenter> {
        /**
         * 更新列表的关注状态
         *
         * @param index       需要更新的item位置
         * @param followState 详见FollowFansBean.class的三种状态值
         */
        void upDateFollowFansState(int index, int followState);
    }

    interface Presenter extends ITSListPresenter<FollowFansBean> {
        /**
         * 重写获取网络数据的方法，添加方法参数
         *
         * @param maxId
         * @param isLoadMore
         * @param userId     用户id
         * @param pageType   详见FollowFansListFragment.class定义的页面类型
         */
        void requestNetData(int maxId, boolean isLoadMore, int userId, int pageType);

        List<FollowFansBean> requestCacheData(int maxId, boolean isLoadMore, int userId, int pageType);

        /**
         * 关注用户
         *
         * @param index      item所在的列表位置
         * @param followedId 被关注的用户id
         */
        void followUser(int index, long followedId);

        void cancleFollowUser(int index, long followedId);
    }

    interface Repository {

        Observable<BaseJson<List<FollowFansBean>>> getFollowListFromNet(long userId, int maxId);

        Observable<BaseJson<List<FollowFansBean>>> getFansListFromNet(long userId, int maxId);

        Observable<BaseJson> followUser(long followedId);

        Observable<BaseJson> cancleFollowUser(long followedId);

    }

}
