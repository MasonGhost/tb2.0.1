package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;

import java.util.List;

import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public interface IBasePublishQuestionRepository {
    Observable<List<QATopicBean>> getAllTopic(String name, Long after, Long follow);

    Observable<List<QATopicBean>> getFollowTopic(String type, Long after);

    Observable<List<QAListInfoBean>> getQAQuestion(String subject, Long maxId, String type);

    Observable<List<QAListInfoBean>> getUserQAQustion(String type,Long after);

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
}
