package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_ALL_POSTLIST;

/**
 * @author Jliuer
 * @Date 2017/11/21/15:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface IBaseCircleRepository {

    Observable<List<CircleTypeBean>> getCategroiesList(int limit, int offet);

    Observable<BaseJsonV2<CircleInfo>> createCircle(CreateCircleBean createCircleBean);

    /**
     * 修改圈子信息
     *
     * @param createCircleBean
     * @return
     */
    Observable<BaseJsonV2<CircleInfo>> updateCircle(CreateCircleBean createCircleBean);

    Observable<BaseJsonV2<CirclePostListBean>> sendCirclePost(PostPublishBean publishBean);

    Observable<List<CirclePostListBean>> getPostListFromCircle(long circleId, long maxId, String type);

    /**
     * 获取我的帖子列表
     *
     * @param limit  默认 15 ，数据返回条数 默认为15
     * @param offset 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param type   参数 type 默认 1，   1-发布的 2- 已置顶 3-置顶待审
     * @return
     */
    Observable<List<CirclePostListBean>> getMinePostList(Integer limit, Integer offset, Integer type);

    /**
     * 全部帖子列表包含搜索
     *
     * @param limit    默认 15 ，数据返回条数 默认为15
     * @param offset   默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param keyword  搜索关键词，模糊匹配圈子名称
     * @param group_id 获取某个圈子下面的全部帖子
     * @return
     */
    @GET(APP_PATH_GET_ALL_POSTLIST)
    Observable<List<CirclePostListBean>> getAllePostList(Integer limit, Integer offset, String keyword, Integer group_id);

    Observable<List<CircleInfo>> getMyJoinedCircle(int limit, int offet, String type);

    /**
     * 获取全部圈子
     *
     * @param limit       默认 15 ，数据返回条数 默认为15
     * @param offet       默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param keyword     用于搜索圈子，按圈名搜索
     * @param category_id 圈子分类id
     * @return
     */
    Observable<List<CircleInfo>> getAllCircle(Integer limit, Integer offet, String keyword
            , Integer category_id);

    Observable<BaseJsonV2<Integer>> getCircleCount();

    void sendPostComment(String commentContent, Long postId, Long replyToUserId, Long commentMark);

    void deletePostComment(long postId, long commentId);

    void deletePost(long circleId, long postId);

    void dealLike(boolean isLike, long postId);

    void dealCollect(boolean isCollected, long postId);

    Observable<BaseJsonV2<Object>> dealCircleJoinOrExit(CircleInfo circleInfo);

    Observable<List<RewardsListBean>> getPostRewardList(long post_id, Integer limit,
                                                        Integer offset, String order,
                                                        String order_type);

    Observable<List<PostDigListBean>> getPostDigList(long postId, int limit, long offet);

    /**
     * 将某个成员踢出圈子
     *
     * @return
     */
    Observable<BaseJsonV2<Object>> cancleCircleMember(long circleId, long memberId);

    /**
     * 指定/撤销圈子管理员职位
     *
     * @param circleId
     * @param memberId
     * @return
     */
    Observable<BaseJsonV2<Object>> appointCircleManager(long circleId, long memberId);

    Observable<BaseJsonV2<Object>> cancleCircleManager(long circleId, long memberId);

    /**
     * 加入/移除圈子黑名单
     *
     * @param circleId
     * @param memberId
     * @return
     */
    Observable<BaseJsonV2<Object>> appointCircleBlackList(long circleId, long memberId);

    Observable<BaseJsonV2<Object>> cancleCircleBlackList(long circleId, long memberId);

}
