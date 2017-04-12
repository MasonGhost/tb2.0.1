package com.zhiyicx.thinksnsplus.modules.dynamic;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/24
 * @Contact master.jungle68@gmail.com
 */

public interface IDynamicReppsitory {
    /**
     * publish dynamic
     *
     * @param dynamicDetailBean dynamic content
     * @param channel_id        如果动态是被发送到频道，需要channel_id
     * @param dynamicBelong     判断动态是被发送到哪儿
     * @return basejson, object is null
     */
    Observable<BaseJson<Object>> sendDynamic(DynamicDetailBean dynamicDetailBean, int dynamicBelong, long channel_id);

    /**
     * get dynamic list
     *
     * @param type       "" 代表最新；follows 代表关注 ； hots 代表热门
     * @param max_id     用来翻页的记录id(对应数据体里的feed_id ,最新和关注选填)
     * @param page       页码 热门选填
     * @param feed_ids   可以是以逗号隔开的id  可以是数组
     * @param isLoadMore 是否是刷新
     * @return dynamic list
     */
    Observable<BaseJson<List<DynamicBean>>> getDynamicList(String type, Long max_id, int page,Long[] feed_ids, boolean isLoadMore);

    /**
     * 动态点赞
     *
     * @param feed_id
     * @return
     */
    void handleLike(boolean isLiked, final Long feed_id);

    /**
     * 删除动态
     *
     * @param feed_id
     */
    void deleteDynamic(final Long feed_id);

    /**
     * 删除评论
     *
     * @param feed_id
     * @param comment_id
     */
    void deleteComment(final Long feed_id, Long comment_id);

    /**
     * 发送评论
     *
     * @param commentContent
     * @param feed_id
     * @param reply_to_user_id
     * @param comment_mark
     */
    void sendComment(String commentContent, final Long feed_id, Long reply_to_user_id, Long comment_mark);

    /**
     * 插入或者更新动态列表
     *
     * @param datas
     * @param type
     */
    void updateOrInsertDynamic(List<DynamicBean> datas, String type);

    /**
     * 取消动态点赞
     *
     * @param feed_id
     * @return
     */
    Observable<BaseJson<String>> cancleLikeDynamic(Long feed_id);

    /**
     * 动态收藏
     *
     * @param feed_id
     * @return
     */
    Observable<BaseJson<Object>> collectDynamic(Long feed_id);


    /**
     * 取消动态收藏
     */
    Observable<BaseJson<Object>> cancleCollectDynamic(Long feed_id);

    /**
     * 获取动态点赞列表
     */
    Observable<BaseJson<List<FollowFansBean>>> getDynamicDigList(Long feed_id, Long max_id);

    /**
     * 一条动态的评论列表
     *
     * @param feed_mark dyanmic feed mark
     * @param feed_id   dyanmic detail id
     * @param max_id    max_id
     * @return
     */
    Observable<BaseJson<List<DynamicCommentBean>>> getDynamicCommentList(Long feed_mark, Long feed_id, Long max_id);

    /**
     * 根据 id 获取评论列表
     *
     * @param comment_ids 评论id 以逗号隔开或者数组形式传入
     * @param feed_mark dyanmic feed mark
     * @return
     */
    Observable<BaseJson<List<DynamicCommentBean>>> getDynamicCommentListByCommentIds(Long[] comment_ids,Long feed_mark);

    /**
     * 增加动态浏览量
     *
     * @param feed_id 动态的唯一 id
     * @return
     */
    void handleDynamicViewCount(Long feed_id);
}