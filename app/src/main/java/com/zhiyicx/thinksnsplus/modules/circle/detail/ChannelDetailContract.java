package com.zhiyicx.thinksnsplus.modules.circle.detail;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseChannelRepository;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public interface ChannelDetailContract {
    interface View extends ITSListView<GroupDynamicListBean, Presenter> {
        /**
         * 所有接口都请求完毕后回调
         */
        void allDataReady(GroupZipBean groupZipBean);

        /**
         * 加载失败
         */
        void loadAllError();

        /**
         * 获取圈子id
         *
         * @return
         */
        long getGroupId();

        /**
         * 处理订阅后的状态
         *
         * @param stateSuccess 订阅是否成功
         * @param message      接口返回message
         */
        void subscribChannelState(boolean stateSuccess, GroupInfoBean channelSubscripBean, String message);

        /**
         * 动态已发送的ui通知
         */
        void sendDynamic();

    }

    interface Repository extends IBaseChannelRepository {
        Observable<GroupInfoBean> getGroupDetail(long group_id);
    }

    interface Presenter extends ITSListPresenter<GroupDynamicListBean> {
        /**
         * 处理用户订阅状态
         */
        void handleGroupSubscrib(GroupInfoBean channelSubscripBean);

        void getGroupDetails(long group_id);

        /**
         * handle like status
         *
         * @param isLiked true,do like ,or  cancle like
         * @param dynamic_id dynamic id
         * @param group_id group id
         * @param position dynamic position
         */
        void handleLike(boolean isLiked, long group_id, long dynamic_id,int position);

        /**
         * add viewcount
         *
         * @param feed_id
         * @param position
         */
        void handleViewCount(Long feed_id, int position);

        /**
         * resend dynamic
         *
         * @param position
         */
        void reSendDynamic(int position);

        /**
         * 重发评论
         *
         * @param commentBean
         * @param feed_id
         */
        void reSendComment(GroupDynamicCommentListBean commentBean, long feed_id);

        /**
         * 删除动态
         *
         * @param dynamicBean
         * @param position
         */
        void deleteDynamic(GroupDynamicListBean dynamicBean,int position);

        /**
         * send a comment
         *
         * @param mCurrentPostion current dynamic position
         * @param replyToUserId   comment  to who
         * @param commentContent  comment content
         */
        void sendComment(int mCurrentPostion, long replyToUserId, String commentContent);

        /**
         * 通过 feedMark 获取当前数据的位置
         *
         * @param feed_id
         * @return
         */
        int getCurrenPosiotnInDataList(long feed_id);

        /**
         * 处理收藏逻辑
         */
        void handleCollect(GroupDynamicListBean dynamicBean);

        /**
         * 动态分享
         */
        void shareDynamic(GroupDynamicListBean dynamicBean, Bitmap bitmap);

        void deleteComment(GroupDynamicListBean dynamicBean, int dynamicPosition, long comment_id, int commentPositon);
    }
}
