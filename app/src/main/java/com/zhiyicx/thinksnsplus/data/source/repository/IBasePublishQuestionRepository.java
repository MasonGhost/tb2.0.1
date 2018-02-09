package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QuestionConfig;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public interface IBasePublishQuestionRepository {

    /**
     *
     * @return 获取问答基础配置
     */
    Observable<QuestionConfig> getQuestionConfig();

    Observable<List<QATopicBean>> getAllTopic(String name, Long after, Long follow);

    Observable<List<QATopicBean>> getFollowTopic(String type, Long after);

    Observable<List<QAListInfoBean>> getQAQuestion(String subject, Long maxId, String type);

    Observable<List<QAListInfoBean>> getUserQAQustion(String type, Long after);

    Observable<BaseJsonV2<AnswerInfoBean>> payForOnlook(Long answer_id);

    Observable<List<QAListInfoBean>> getQAQuestionByTopic(String topicId, String subject, Long maxId, String type);

    Observable<List<ExpertBean>> getTopicExperts(Long maxId, int topic_id);

    void handleTopicFollowState(String topic_id, boolean isFollow);

    void handleQuestionFollowState(String questionId, boolean isFollow);

    void handleAnswerLike(boolean isLiked, final long answer_id);

    void saveQuestion(QAPublishBean qestion);

    void deleteQuestion(QAPublishBean qestion);

    QAPublishBean getDraftQuestion(long qestion_mark);

    void saveAnswer(AnswerDraftBean answer);

    void deleteAnswer(AnswerDraftBean answer);

    AnswerDraftBean getDraftAnswer(long answer_mark);

    Observable<QAListInfoBean> getQuestionDetail(String questionId);

    Observable<List<AnswerInfoBean>> getAnswerList(String questionId, String order_type, int size);

    Observable<BaseJsonV2<Object>> deleteQuestion(Long question_id);

    Observable<BaseJsonV2<Object>> applyForExcellent(Long question_id);

    Observable<List<ExpertBean>> getExpertList(int size, String topic_ids, String keyword);

    Observable<QATopicBean> getTopicDetail(String topic_id);

    Observable<BaseJsonV2<AnswerInfoBean>> publishAnswer(Long question_id, String body, String text_body, int anonymity);

    Observable<BaseJsonV2<Object>> updateAnswer(Long answer_id, String body, String text_body, int anonymity);

    Observable<BaseJsonV2<Object>> updateQuestion(Long question_id, String body, int anonymity);

    Observable<BaseJsonV2> createTopic(String name, String desc);

    Observable<List<AnswerDigListBean>> getAnswerDigListV2(Long answer_id, Long max_id);

    Observable<List<AnswerCommentListBean>> getAnswerCommentList(long answer_id,
                                                                 long max_id);

    Observable<AnswerInfoBean> getAnswerDetail(long answer_id);

    void handleCollect(boolean isCollected, long answer_id);

    void sendComment(String comment_content, long answer_id,
                     long reply_to_user_id, long comment_mark);

    void deleteComment(long answer_id, long comment_id);

    void deleteAnswer(long answer_id);

    Observable<BaseJsonV2<Object>> adoptionAnswer(long question_id, long answer_id);

    Observable<List<QuestionCommentBean>> getQuestionCommentList(Long question_Id, Long max_id);

    void sendQuestionComment(String comment_content, long question_id,
                             long reply_to_user_id, long comment_mark);

    Observable<BaseJsonV2<Object>> deleteQuestionComment(long question_id, long answer_id);

    Observable<List<AnswerInfoBean>> getUserAnswerList(String type, Long maxId);

    /**
     * 获取用户收藏的回答列表
     */
    Observable<List<AnswerInfoBean>> getUserCollectAnswerList(Long limit, Long maxId);

    Observable<Object> publishQuestion(QAPublishBean qaPublishBean);

    Observable<Object> updateQuestion(QAPublishBean qaPublishBean);

    Observable<BaseJsonV2<Object>> resetReward(Long question_id, double amount);

}
