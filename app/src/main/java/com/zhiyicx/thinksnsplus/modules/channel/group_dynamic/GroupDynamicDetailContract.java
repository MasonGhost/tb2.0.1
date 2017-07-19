package com.zhiyicx.thinksnsplus.modules.channel.group_dynamic;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/18
 * @contact email:648129313@qq.com
 */

public interface GroupDynamicDetailContract {

    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<GroupDynamicCommentListBean, Presenter> {

        void initDynamicDetail(GroupDynamicListBean groupDynamicListBean);

        /**
         * 设置是否喜欢该动态
         *
         * @param isLike
         */
        void setLike(boolean isLike);

        /**
         * 设置是否收藏该动态
         *
         * @param isCollect
         */
        void setCollect(boolean isCollect);

        /**
         * 设置点赞头像
         */
        void setDigHeadIcon(List<FollowFansBean> userInfoBeanList);

        /**
         * 更新关注状态
         */
        void upDateFollowFansState(int followState);

        /**
         * 设置初始关注状态
         */
        void initFollowState(FollowFansBean mFollowFansBean);

        /**
         * 获取当前动态数据
         */
        GroupDynamicListBean getCurrentDynamic();

        /**
         * 获取当前动态在列表中的位置
         *
         * @return
         */
        Bundle getArgumentsBundle();

        /**
         * 刷新评论数,喜欢
         */
        void updateCommentCountAndDig();

        /**
         * 所有数据都有了，直接显示
         */
        void allDataReady();

        /**
         * 加载失败
         */
        void loadAllError();

        /**
         * 动态已经被删除了
         */
        void dynamicHasBeDeleted();

        void updateDynamic(GroupDynamicListBean groupDynamicListBean);

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IDynamicReppsitory {
        /**
         * 获取用户关注状态
         *
         * @param user_ids
         * @return
         */
        Observable<BaseJson<List<FollowFansBean>>> getUserFollowState(String user_ids);
    }

    interface Presenter extends ITSListPresenter<GroupDynamicCommentListBean> {
        /**
         * 获取当前动态
         *
         * @param feed_id
         */
        void getCurrentDynamic(long feed_id);

        /**
         * 获取当前动态详情 V2
         *
         * @param feed_id
         */
        void getCurrentDynamicDetail(long feed_id,int topFlag);

        /**
         * 获取当前动态的点赞列表
         */
        void getDetailAll(Long feed_id, Long max_id, String user_ids,int topFlag);

        /**
         * 获取当前动态的点赞列表
         */
        void getDynamicDigList(Long feed_id, Long max_id);

        /**
         * 处理喜欢逻辑
         *
         * @param groupDynamicListBean 更新数据库
         */
        void handleLike(boolean isLiked, Long feed_id, GroupDynamicListBean groupDynamicListBean);

        /**
         * 处理收藏逻辑
         */

        void handleCollect(GroupDynamicListBean groupDynamicListBean);

        /**
         * 动态分享
         */
        void shareDynamic(GroupDynamicListBean groupDynamicListBean, Bitmap bitmap);

        /**
         * 关注或者取消关注
         */
        void handleFollowUser(FollowFansBean followFansBean);

        /**
         * 获取关注状态
         */
        void getUserFollowState(String user_ids);

        /**
         * send a comment
         *
         * @param replyToUserId  comment  to who
         * @param commentContent comment content
         */
        void sendComment(long replyToUserId, String commentContent);
        void sendCommentV2(long replyToUserId, String commentContent);

        /**
         * delete a comment
         *
         * @param comment_id      comment's id
         * @param commentPosition comment curren position
         */
        void deleteComment(long comment_id, int commentPosition);
        void deleteCommentV2(long comment_id, int commentPosition);

        /**
         * check current dynamic is has been deleted
         *
         * @param user_id   the dynamic is belong to
         * @param feed_mark the dynamic's feed_mark
         * @return
         */
        boolean checkCurrentDynamicIsDeleted(Long user_id, Long feed_mark);

        List<SystemConfigBean.Advert> getAdvert();

        void checkNote(int note);

        void payNote(int imagePosition,int note,boolean isImage);
    }
}
