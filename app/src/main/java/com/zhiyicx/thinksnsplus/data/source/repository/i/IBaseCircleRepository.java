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

    Observable<List<CirclePostListBean>> getPostListFromCircle(long circleId, long maxId);

    Observable<List<CircleInfo>> getMyJoinedCircle(int limit, int offet);

    Observable<List<CircleInfo>> getAllCircle(int limit, int offet);

    Observable<BaseJsonV2<Integer>> getCircleCount();

    void sendPostComment(String commentContent, Long postId, Long replyToUserId, Long commentMark);

    void deletePostComment(long postId, long commentId);

    void deletePost(long circleId, long postId);

    void dealLike(boolean isLike, long postId);

    void dealCollect(boolean isCollected, long postId);

    Observable<BaseJsonV2<Object>> dealCircleJoinOrExit(CircleInfo circleInfo);

    Observable<List<RewardsListBean>> getPostRewardList(long postId, int limit, long offet);

    Observable<List<PostDigListBean>> getPostDigList(long postId, int limit, long offet);

}
