package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.TopCircleJoinReQuestBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.TopNewsCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopPostCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopPostListBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe 审核相关
 * @Author Jungle68
 * @Date 2017/12/25
 * @Contact master.jungle68@gmail.com
 */
public interface IMessageReviewRepository {
    Observable<List<TopDynamicCommentBean>> getDynamicReviewComment(int after);

    Observable<List<TopNewsCommentListBean>> getNewsReviewComment(int after);

    Observable<List<TopPostCommentListBean>> getPostReviewComment(int after);

    Observable<List<TopCircleJoinReQuestBean>> getCircleJoinRequest(int after);

    Observable<List<TopPostListBean>> getPostReview(Long circleId, int after);

    Observable<BaseJsonV2> approvedTopComment(Long feed_id, int comment_id, int pinned_id);

    Observable<BaseJsonV2> refuseTopComment(int pinned_id);

    Observable<BaseJsonV2> approvedNewsTopComment(Long feed_id, int comment_id, int pinned_id);

    Observable<BaseJsonV2> refuseNewsTopComment(int news_id, Long comment_id, int pinned_id);

    Observable<BaseJsonV2> deleteTopComment(Long feed_id, int comment_id);

    Observable<BaseJsonV2> approvedPostTopComment(Integer comment_id);

    Observable<BaseJsonV2> refusePostTopComment(Integer comment_id);

    Observable<BaseJsonV2> approvedPostTop(Long psotId);

    Observable<BaseJsonV2> refusePostTop(Long psotId);

    Observable<BaseJsonV2> refuseCircleJoin(BaseListBean result);

    Observable<BaseJsonV2> approvedCircleJoin(Long feedId, int commentId);
}
