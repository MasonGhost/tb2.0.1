package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/14
 * @Contact master.jungle68@gmail.com
 */

public interface IReportRepository {


    /**
     * 举报一条动态
     *
     * @param feedId 动态 id
     * @param reason 举报原因
     * @return
     */
    Observable<ReportResultBean> reportDynamic(String feedId, String reason);

    /**
     * 举报一条资讯
     *
     * @param newsId 资讯 id
     * @param reason 举报原因
     * @return
     */
    Observable<ReportResultBean> reportInfo(String newsId, String reason);


    /**
     * 举报一条问题
     *
     * @param questionId 动态 id
     * @param reason     举报原因
     * @return
     */
    Observable<ReportResultBean> reportQA(String questionId, String reason);


    /**
     * 举报一条答案
     *
     * @param answerId 动态 id
     * @param reason   举报原因
     * @return
     */
    Observable<ReportResultBean> reportQAAnswer(String answerId, String reason);


    /**
     * 举报圈子
     *
     * @param groupId 圈子 id
     * @param reason  举报原因
     * @return
     */
    Observable<ReportResultBean> reportCircle(String groupId, String reason);

    /**
     * 举报圈子中的帖子
     *
     * @param postId 帖子 id
     * @param reason 举报原因
     * @return
     */
    Observable<ReportResultBean> reportCirclePost(String postId, String reason);

    /**
     * 举报评论
     *
     * @param commentId 评论 id
     * @param reason    举报原因
     * @return
     */
    Observable<ReportResultBean> reportComment(String commentId, String reason);
}
