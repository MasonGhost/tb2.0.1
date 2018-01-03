package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface IBaseChannelRepository extends IDynamicReppsitory {

    /**
     * 在fragment中处理订阅状态
     */
    Observable<BaseJsonV2<Object>> handleSubscribGroupByFragment(GroupInfoBean channelSubscripBean);
    /**
     * 获取圈子的动态列表
     */
    Observable<List<GroupDynamicListBean>> getDynamicListFromGroup(long group_id, long max_id);
    /**
     * 获取圈子我收藏的动态列表
     */
    Observable<List<GroupDynamicListBean>> getMyCollectGroupDynamicList(long group_id, long max_id);

    /**
     * 获取圈子列表
     *
     * @param type 0-全部 1-加入的
     */
    Observable<List<GroupInfoBean>> getGroupList(int type, long max_id);

    void handleGroupJoin(GroupInfoBean groupInfoBean);

    void sendGroupComment(String commentContent,Long group_id, Long feed_id, Long reply_to_user_id,Long comment_mark);

    void deleteGroupComment(long group_id,long feed_id,long comment_id);

    void deleteGroupDynamic(long group_id,long feed_id);

    Observable<BaseJsonV2<Object>> sendGroupDynamic(GroupSendDynamicDataBean dynamicDetailBean);

    /**
     * 获取圈子动态的评论列表
     */
    Observable<List<GroupDynamicCommentListBean>> getGroupDynamicCommentList(long group_id, long dynamic_id, long max_id);

    /**
     * 获取圈子动态详情
     *
     * @param group_id   圈子id
     * @param dynamic_id 动态id
     */
    Observable<GroupDynamicListBean> getGroupDynamicDetail(long group_id, long dynamic_id);

    Observable<List<DynamicDigListBean>> getGroupDynamicDigList(long group_id, long dynamic_id, long max_id);

    /**
     * 圈子动态点赞
     */
    void handleLike(boolean isLiked, long group_id, long dynamic_id);

    /**
     * 圈子动态收藏
     */
    void handleCollect(boolean isCollected, long group_id, long dynamic_id);




    /**
     * 获取全部圈子列表
     */
    Observable<List<GroupInfoBean>> getAllGroupList(long max_id);

    /**
     * 获取用户加入的圈子
     */
    Observable<List<GroupInfoBean>> getUserJoinedGroupList(long max_id);
}
