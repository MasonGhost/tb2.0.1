package com.zhiyicx.thinksnsplus.modules.personal_center;

import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public interface PersonalCenterContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicBean, PersonalCenterContract.Presenter> {
        /**
         * 设置头部的用户相关信息
         */
        void setHeaderInfo(UserInfoBean userInfoBean);

        /**
         * 设置关注状态
         *
         * @param followFansBean
         */
        void setFollowState(FollowFansBean followFansBean);

        /**
         * 设置封面上传的状态
         *
         * @param taskId 返回的图片任务id
         */
        void setUpLoadCoverState(boolean upLoadState, int taskId);

        /**
         * 设置通知服务器封面更新的状态
         */
        void setChangeUserCoverState(boolean changeSuccess);

        /**
         * 获取列表数据
         *
         * @return
         */
        List<DynamicBean> getDatas();

        /**
         * 获取列表数据
         *
         * @return
         */
        void refresh();

        void refresh(int position);

        void allDataReady();

        /**
         * 加载失败
         */
        void loadAllError();
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IDynamicReppsitory {
        /**
         * 获取当前个人中心的用户信息
         *
         * @return
         */
        Observable<BaseJson<UserInfoBean>> getCurrentUserInfo(Long userId);

        /**
         * 获取某个人的动态列表
         */
        Observable<BaseJson<List<DynamicBean>>> getDynamicListForSomeone(Long user_id, Long max_id);

        /**
         * 获取用户关注状态
         *
         * @param user_ids
         * @return
         */
        Observable<BaseJson<FollowFansBean>> getUserFollowState(String user_ids);
    }

    interface Presenter extends DynamicContract.Presenter {

        void setCurrentUserInfo(Long userId);

        /**
         * 从网络获取该用户的动态列表
         *
         * @param maxId
         * @param isLoadMore
         * @param user_id
         */
        void requestNetData(Long maxId, boolean isLoadMore, long user_id);

        /**
         * 从数据库获取该用户的动态列表
         *
         * @param max_Id     当前获取到数据的最小时间
         * @param isLoadMore 加载状态
         * @param user_id
         * @return
         */
        List<DynamicBean> requestCacheData(Long max_Id, boolean isLoadMore, long user_id);

        /**
         * 设置进入个人中心是的用户关注状态
         */
        void initFollowState(long user_id);

        /**
         * 处理关注状态
         */
        void handleFollow(FollowFansBean followFansBean);

        /**
         * 上传封面图片
         */
        void uploadUserCover(String filePath);

        /**
         * 修改封面图片
         *
         * @param storage_task_id 封面上传的任务id
         * @param imagePath       上传封面的本地路径
         */
        void changeUserCover(UserInfoBean userInfoBean, int storage_task_id, String imagePath);
    }
}
