package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface IBaseCircleRepository {
    Observable<List<CircleTypeBean>> getCategroiesList(int limit, int offet);

    Observable<BaseJsonV2<CircleInfo>> createCircle(CreateCircleBean createCircleBean);

    Observable<BaseJsonV2<CirclePostListBean>> sendCirclePost(PostPublishBean publishBean);

    Observable<List<CirclePostListBean>> getPostListFromCircle(long circleId, long maxId,String type);

    Observable<List<CircleInfo>> getMyJoinedCircle(int limit, int offet);

    Observable<List<CircleInfo>> getAllCircle(int limit, int offet);

    Observable<BaseJsonV2<Integer>> getCircleCount();

    void sendPostComment(String commentContent, Long postId, Long replyToUserId, Long commentMark);

    void deletePostComment(long postId, long commentId);

    void deletePost(long circleId, long postId);

    void dealLike(boolean isLike, long postId);

    void dealCollect(boolean isCollected, long postId);

    Observable<BaseJsonV2<Object>> dealCircleJoinOrExit(CircleInfo circleInfo);

    Observable<List<RewardsListBean>> getPostRewardList(long post_id,  Integer limit,
                                                        Integer offset, String order,
                                                        String order_type);

    Observable<List<PostDigListBean>> getPostDigList(long postId, int limit, long offet);

}
