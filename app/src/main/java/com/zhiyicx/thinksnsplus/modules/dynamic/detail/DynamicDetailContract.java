package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */

public interface DynamicDetailContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicCommentBean, Presenter> {
        void initDynamicDetial(DynamicDetailBeanV2 dynamicBean);

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
        void setDigHeadIcon(List<DynamicDigListBean> userInfoBeanList);

        /**
         * 更新关注状态
         */
        void upDateFollowFansState(UserInfoBean userInfoBean);

        /**
         * 获取当前动态数据
         */
        DynamicDetailBeanV2 getCurrentDynamic();

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

        void updateDynamic(DynamicDetailBeanV2 detailBeanV2);

        void setRewardListBeans(List<RewardsListBean> rewardsListBeens);

        void updateReward();
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IDynamicReppsitory {

    }

    interface Presenter extends ITSListPresenter<DynamicCommentBean> {

        /**
         * 获取当前动态详情 V2
         *
         * @param feed_id
         */
        void getCurrentDynamicDetail(long feed_id, int topFlag);

        /**
         * 获取当前动态的点赞列表
         */
        void getDetailAll(Long feed_id, Long max_id, String user_ids, int topFlag);

        /**
         * 获取当前动态的点赞列表
         */
        void getDynamicDigList(Long feed_id, Long max_id);

        /**
         * 处理喜欢逻辑
         *
         * @param dynamicToolBean 更新数据库
         */
        void handleLike(boolean isLiked, Long feed_id, DynamicDetailBeanV2 dynamicToolBean);

        /**
         * 处理收藏逻辑
         */

        void handleCollect(DynamicDetailBeanV2 dynamicBean);

        /**
         * 动态分享
         */
        void shareDynamic(DynamicDetailBeanV2 dynamicBean, Bitmap bitmap);

        /**
         * 关注或者取消关注
         */
        void handleFollowUser(UserInfoBean userInfoBean);

        /**
         * send a comment
         *
         * @param replyToUserId  comment  to who
         * @param commentContent comment content
         */
        void sendCommentV2(long replyToUserId, String commentContent);

        void reSendComment(DynamicCommentBean commentBean, long feed_id);

        /**
         * delete a comment
         *
         * @param comment_id      comment's id
         * @param commentPosition comment curren position
         */
        void deleteCommentV2(long comment_id, int commentPosition);

        /**
         * check current dynamic is has been deleted
         *
         * @param user_id   the dynamic is belong to
         * @param feed_mark the dynamic's feed_mark
         * @return
         */
        boolean checkCurrentDynamicIsDeleted(Long user_id, Long feed_mark);

        /**
         * @return
         */
        List<RealAdvertListBean> getAdvert();

        /**
         * @param note
         */

        void checkNote(int note);

        /**
         * pay note
         *
         * @param imagePosition image position
         * @param note
         * @param isImage
         */
        void payNote(int imagePosition, int note, boolean isImage);

        /**
         * update reward data
         *
         * @param id  source id
         */
        void updateRewardData(Long id);

        void allDataReady();

        void setNeedDynamicListRefresh(boolean needDynamicListRefresh);

    }
}
