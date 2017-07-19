package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicLikeListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

import retrofit2.http.Path;
import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface IBaseChannelRepository extends IDynamicReppsitory {

    /**
     * 在server处理订阅状态
     */

    void handleSubscribChannel(ChannelSubscripBean channelSubscripBean);

    /**
     * 在fragment中处理订阅状态
     */
    Observable<BaseJsonV2<Object>> handleSubscribGroupByFragment(GroupInfoBean channelSubscripBean);

    /**
     * 获取频道列表
     *
     * @param type   type 频道类型 “”表示所有的频道  “my”表示我关注的频道 在APiConfig中定义了这两个常量
     * @param userId 请求频道列表的用户,作为拓展字段如果有其他用户需要请求频道列表，使用该字段
     */
    Observable<BaseJson<List<ChannelSubscripBean>>> getChannelList(@Path("type") String type, long userId);

    /**
     * 获取频道的动态列表
     */
    Observable<List<GroupDynamicListBean>> getDynamicListFromGroup(long group_id, long max_id);

    /**
     * 获取圈子列表
     *
     * @param type 0-全部 1-加入的
     */
    Observable<List<GroupInfoBean>> getGroupList(int type, long max_id);

    void handleGroupJoin(GroupInfoBean groupInfoBean);

    void sendGroupComment(String commentContent,Long group_id, Long feed_id, Long reply_to_user_id,Long comment_mark);

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

    Observable<List<GroupDynamicLikeListBean>> getGroupDynamicDigList(long group_id, long dynamic_id, long max_id);

    /**
     * 圈子动态点赞
     */
    void handleLike(boolean isLiked, long group_id, long dynamic_id);

    /**
     * 圈子动态收藏
     */
    void handelCollect(boolean isCollected, long group_id, long dynamic_id);
}
