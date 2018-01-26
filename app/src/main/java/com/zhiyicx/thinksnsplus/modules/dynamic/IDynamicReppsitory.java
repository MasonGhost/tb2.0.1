package com.zhiyicx.thinksnsplus.modules.dynamic;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentToll;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;

import java.util.List;

import retrofit2.http.Query;
import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/24
 * @Contact master.jungle68@gmail.com
 */

public interface IDynamicReppsitory {
    /**
     * publish dynamic V2
     *
     * @param dynamicDetailBean dynamic content
     * @return basejson, object is null
     */
    Observable<BaseJsonV2<Object>> sendDynamicV2(SendDynamicDataBeanV2 dynamicDetailBean);


    /**
     * get dynamic list
     *
     * @param type       "" 代表最新；follows 代表关注 ； hots 代表热门
     * @param after     用来翻页的记录id(对应数据体里的 feed_id ,最新和关注选填)
     * @param user_id   动态所属人
     * @param isLoadMore 是否是刷新
     * @param screen  type = users 时可选，paid-付费动态 pinned - 置顶动态
     * @return dynamic list
     */
    Observable<List<DynamicDetailBeanV2>> getDynamicListV2(String type, Long after, Long user_id,boolean isLoadMore,
                                                           String screen);

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
    void deleteCommentV2(final Long feed_id, Long comment_id);

    /**
     * 发送评论
     *
     * @param commentContent
     * @param feed_id
     * @param reply_to_user_id
     * @param comment_mark
     */
    void sendCommentV2(String commentContent, final Long feed_id, Long reply_to_user_id, Long comment_mark);



    void updateOrInsertDynamicV2(List<DynamicDetailBeanV2> datas, String type);


    Observable<List<DynamicDigListBean>> getDynamicDigListV2(Long feed_id, Long max_id);

    /**
     * 一条动态的评论列表
     *
     * @param feed_mark dyanmic feed mark
     * @param feed_id   dyanmic detail id
     * @param max_id    max_id
     * @return
     */
    Observable<List<DynamicCommentBean>> getDynamicCommentListV2(Long feed_mark, Long feed_id, Long max_id);


    /**
     * 获取动态详情 V2
     *
     * @param feed_id 动态id
     * @return
     */
    Observable<DynamicDetailBeanV2> getDynamicDetailBeanV2(Long feed_id);

    /**
     * 设置动态评论收费 V2
     *
     * @param feed_id 动态id
     * @param amout   收费金额
     * @return
     */
    Observable<DynamicCommentToll> setDynamicCommentToll(Long feed_id, int amout);

    Observable<BaseJsonV2<Integer>> stickTop(long feed_id, double amount, int day);
    Observable<BaseJsonV2<Integer>> commentStickTop(long feed_id,long comment_id, double amount, int day);
    Observable<DynamicCommentToll> tollDynamicComment(Long feed_id, int amount);

    /**
     * 获取某个人的动态列表
     */
    Observable<List<DynamicDetailBeanV2>> getDynamicListForSomeone(Long user_id, Long max_id, String screen);

}