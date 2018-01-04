package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public interface PersonalCenterContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicDetailBeanV2, PersonalCenterContract.Presenter> {
        /**
         * 设置头部的用户相关信息
         */
        void setHeaderInfo(UserInfoBean userInfoBean);

        /**
         * 设置关注状态
         *
         * @param followFansBean
         */
        void setFollowState(UserInfoBean followFansBean);

        /**
         * 设置封面上传的状态
         */
        void setUpLoadCoverState(boolean upLoadState);

        /**
         * 设置通知服务器封面更新的状态
         */
        void setChangeUserCoverState(boolean changeSuccess);

        /**
         * 所有接口都请求完毕后回调
         */
        void allDataReady();

        /**
         * 加载失败
         */
        void loadAllError();

        /**
         * 动态改变的数量
         *
         * @param changeNums
         */
        void updateDynamicCounts(int changeNums);

        /**
         * 获取用户头像，加快分享速度
         *
         * @return
         */
        Bitmap getUserHeadPic();

        /**
         * @return null-全部 paid-付费动态 pinned - 置顶动态
         */
        String getDynamicType();

        /**
         * 选择的动态类型修改  {@@link MyDynamicTypeEnum}
         *
         * @param type
         */
        void onDynamicTypeChanged(BaseDynamicRepository.MyDynamicTypeEnum type);

        /**
         * 刷新回调
         */
        void refreshStart();

        void refreshEnd();
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
        List<DynamicDetailBeanV2> requestCacheData(Long max_Id, boolean isLoadMore, long user_id);

        /**
         * 处理关注状态
         */
        void handleFollow(UserInfoBean followFansBean);

        /**
         * 上传封面图片
         */
        void uploadUserCover(String filePath, UserInfoBean userInfoBean);

        /**
         * 分享用户信息
         *
         * @param userInfoBean
         */
        void shareUserInfo(UserInfoBean userInfoBean);
    }
}
