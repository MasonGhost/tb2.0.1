package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseChannelRepository;

import java.util.List;

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
         */
        void setLike(boolean isLike);

        /**
         * 设置是否收藏该动态
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
         * 设置初始关注状态
         */
        void initFollowState(UserInfoBean userInfoBean);

        /**
         * 获取当前动态数据
         */
        GroupDynamicListBean getCurrentDynamic();

        /**
         * 获取当前动态在列表中的位置
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

    interface Presenter extends ITSListPresenter<GroupDynamicCommentListBean> {

        /**
         * 获取当前动态详情
         *
         * @param group_id   圈子id
         * @param dynamic_id 动态id
         */
        void getCurrentDynamicDetail(long group_id, long dynamic_id,boolean refreshUI);

        /**
         * 获取当前动态的点赞列表
         */
        void getDetailAll(long group_id, long dynamic_id, Long max_id, String user_ids);

        /**
         * 获取当前动态的点赞列表
         */
        void getDynamicDigList(long group_id, long dynamic_id, long max_id);

        /**
         * 处理喜欢逻辑
         *
         * @param groupDynamicListBean 更新数据库
         */
        void handleLike(boolean isLiked, long group_id, long dynamic_id, GroupDynamicListBean groupDynamicListBean);

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
        void handleFollowUser(UserInfoBean followFansBean);

        void sendCommentV2(long replyToUserId, String commentContent);

        void deleteCommentV2(long comment_id, int commentPosition);

        /**
         * check current dynamic is has been deleted
         *
         * @param user_id   the dynamic is belong to
         * @param feed_mark the dynamic's feed_mark
         */
        boolean checkCurrentDynamicIsDeleted(Long user_id, Long feed_mark);

        List<RealAdvertListBean> getAdvert();

        void checkNote(int note);

        void payNote(int imagePosition, int note, boolean isImage);
    }
}
