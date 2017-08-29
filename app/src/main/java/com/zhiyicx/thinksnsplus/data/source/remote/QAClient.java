package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.QAAnswerBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_QA_ANSWER_REWARD;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_QA_ANSWER_REWARD_USER_LIST;

/**
 * @Author Jliuer
 * @Date 2017/08/14/10:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface QAClient {

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST(ApiConfig.APP_PATH_PUBLISH_QUESTIONS)
    Observable<BaseJsonV2<QAPublishBean>> publishQuestion(@Body RequestBody body);

    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_PUBLISH_ANSWER)
    Observable<BaseJsonV2<QAAnswerBean>> publishAnswer(@Path("question") Long question_id, @Field("body") String body, @Field("anonymity") int anonymity);

    /**
     * @param question_id
     * @param body        如果 anonymity 不传，则本字段必须存在， 回答详情。
     * @param anonymity   如果 body 字段不传，则本字段必须存在，是否匿名。
     * @return
     */
    @FormUrlEncoded
    @PATCH(ApiConfig.APP_PATH_GET_QUESTION_DETAIL)
    Observable<BaseJsonV2<Object>> uplaodQuestion(@Path("question") Long question_id, @Field("body")
            String body, @Field("anonymity") int anonymity);

    /**
     * @param answer_id
     * @param body      如果 anonymity 不传，则本字段必须存在， 回答详情。
     * @param anonymity 如果 body 字段不传，则本字段必须存在，是否匿名。
     * @return
     */
    @FormUrlEncoded
    @PATCH(ApiConfig.APP_PATH_UPDATE_ANSWER)
    Observable<BaseJsonV2<Object>> uplaodAnswer(@Path("answer_id") Long answer_id, @Field("body") String body, @Field("anonymity") int anonymity);

    /**
     * @param name   用于搜索话题，传递话题名称关键词。
     * @param after  获取 id 之后的数据，要获取某条话题之后的数据，传递该话题 ID。
     * @param follow 是否检查当前用户是否关注了某话题，默认为不检查，如果传递 follow 切拥有任意值（空除外），都会检查当前用户与话题的关注关系。
     * @param limit  这次请求获取的条数，默认为 20 条，为了避免过大或者错误查询，设置了一个修正值，最大 50 最小 1 。
     */
    @GET(ApiConfig.APP_PATH_GET_ALL_TOPIC)
    Observable<List<QATopicBean>> getQATopic(@Query("name") String name, @Query
            ("after") Long after, @Query("follow") Long follow, @Query("limit") Long limit);

    /**
     * 获取回答的详细信息
     *
     * @param answer_id 回答 id
     */
    @GET(ApiConfig.APP_PATH_GET_ANSWER_DETAIL)
    Observable<AnswerInfoBean> getAnswerDetail(@Path("answer_id") long answer_id);

    /**
     * 获取回答的点赞列表
     *
     * @param answer_id 回答 id
     */
    @GET(ApiConfig.APP_PATH_LIKE_ANSWER)
    Observable<List<AnswerDigListBean>> getAnswerDigList(@Path("answer_id") long answer_id, @Query
            ("after") Long after, @Query("limit") Long limit);

    /**
     * 获取回答评论列表
     *
     * @param answer_id 回答 id
     */
    @GET(ApiConfig.APP_PATH_GET_ANSWER_COMMENTS)
    Observable<List<AnswerCommentListBean>> getAnswerCommentList(@Path("answer_id") long answer_id, @Query
            ("after") Long after, @Query("limit") Long limit);

    /**
     * @param type 默认值为 follow 代表用户关注的话题列表，如果值为 expert 则获取该用户的专家话题（哪些话题下是专家）。
     */
    @GET(ApiConfig.APP_PATH_GET_FOLLOW_TOPIC)
    Observable<List<QATopicBean>> getQAFollowTopic(@Query("type") String type, @Query
            ("after") Long after, @Query("limit") Long limit);

    /**
     * @param subject 用于搜索问题，传递话题名称关键词。
     * @param after   获取 id 之后的数据，要获取某条问题之后的数据，传递该问题 ID。
     * @param type    默认值 new, all - 全部、new - 最新、hot - 热门、reward - 悬赏、excellent - 精选 。
     */
    @GET(ApiConfig.APP_PATH_PUBLISH_QUESTIONS)
    Observable<List<QAListInfoBean>> getQAQustion(@Query("subject") String subject, @Query
            ("offset") Long after, @Query("type") String type, @Query("limit") Long limit);

    /**
     * @param type  数据筛选类型 all-全部 invitation-邀请 reward-悬赏 other-其他 默认全部
     * @param after 获取 id 之后的数据，要获取某条问题之后的数据，传递该问题 ID。
     */
    @GET(ApiConfig.APP_PATH_GET_USER_QUESTIONS)
    Observable<List<QAListInfoBean>> getUserQAQustion(@Query("type") String type, @Query("after") Long after, @Query("limit") Long limit);

    /**
     * 某话题下的问题
     * @param topic_id
     * @param subject
     * @param after
     * @param type
     * @param limit
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_QUESTION_LIST_BY_TOPIC)
    Observable<List<QAListInfoBean>> getQAQustionByTopic(@Path("topic") String topic_id, @Query("subject") String subject, @Query
            ("offset") Long after, @Query("type") String type, @Query("limit") Long limit);

    /**
     * 获取话题下专家列表
     * @param topic_id 话题id
     * @param after
     * @param limit
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_TOPIC_EXPERTS)
    Observable<List<ExpertBean>> getTopicExperts(@Path("topic_id") int topic_id, @Query("after") Long after, @Query("limit") Long limit);

    /**
     * 获取话题详情
     *
     * @param topic_id 话题id
     */
    @GET(ApiConfig.APP_PATH_GET_TOPIC_DETAIL)
    Observable<QATopicBean> getTopicDetail(@Path("topic") String topic_id);

    /**
     * 获取问题详情
     *
     * @param question_id 问题id
     */
    @GET(ApiConfig.APP_PATH_GET_QUESTION_DETAIL)
    Observable<QAListInfoBean> getQuestionDetail(@Path("question") String question_id);

    /**
     * 获取一个问题的回答列表
     *
     * @param question_id 问题id
     * @param order_type  default/time
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_QUESTION_ANSWER_LIST)
    Observable<List<AnswerInfoBean>> getAnswerList(@Path("question") String question_id,
                                                   @Query("limit") Long limit,
                                                   @Query("order_type") String order_type,
                                                   @Query("offset") int size);

    /**
     * 删除问题
     *
     * @param question_id 问题id
     */
    @DELETE(ApiConfig.APP_PATH_GET_DELETE_QUESTION)
    Observable<BaseJsonV2<Object>> deleteQuestion(@Path("question") String question_id);

    /**
     * 申请精选问答
     */
    @POST(ApiConfig.APP_PATH_APPLY_FOR_EXCELLENT)
    Observable<BaseJsonV2<Object>> applyForExcellent(@Path("question") String question_id);

    /**
     * 获取问题的评论列表
     *
     * @param question_id id
     */
    @GET(ApiConfig.APP_PATH_GET_QUESTION_COMMENT_LIST)
    Observable<List<QuestionCommentBean>> getQuestionCommentList(@Path("question") String question_id,
                                                                 @Query("after") Long after,
                                                                 @Query("limit") Long limit);

    /**
     * 删除问题评论
     */
    @DELETE(ApiConfig.APP_PATH_DELETE_QUESTION_COMMENT)
    Observable<BaseJsonV2<Object>> deleteQuestionComment(@Path("question") String question_id, @Path("answer") String answer_id);

    /**
     * 批量获取专家列表
     * @param topic_ids
     * @param size
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_TOPIC_EXPERT_LIST)
    Observable<List<ExpertBean>> getExpertListByTopicIds(@Query("topics") String topic_ids, @Query("offset") int size);

    /*******************************************  打赏  *********************************************/


    /**
     * 对一条问答回答打赏
     *
     * @param answer_id 动态 id
     * @param amount    打赏金额
     */
    @FormUrlEncoded
    @POST(APP_PATH_QA_ANSWER_REWARD)
    Observable<Object> rewardQA(@Path("answer_id") long answer_id, @Field("amount") Integer amount);


    /**
     * 问答回答打赏列表
     *
     * @param answer_id  动态 id
     * @param limit      默认 20 ，获取列表条数，修正值 1 - 30
     * @param offset     默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param order_type 默认值 time, time - 按照打赏时间倒序，amount - 按照金额倒序
     */
    @GET(APP_PATH_QA_ANSWER_REWARD_USER_LIST)
    Observable<List<RewardsListBean>> rewardQAList(@Path("answer_id") long answer_id, @Query("limit") Integer limit
            , @Query("offset") Integer offset, @Query("type") String order_type);

    @PATCH(ApiConfig.APP_PATH_UPDATE_QUESTION_REWARD)
    Observable<BaseJsonV2<Object>> updateQuestionReward(@Path("question") String question_id, @Query("amount") int amount);

}
