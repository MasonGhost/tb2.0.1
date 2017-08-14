package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;

import java.util.List;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public interface IBasePublishQuestionRepository {
    Observable<List<QATopicBean>> getAllTopic(String name, Long after, Long follow);
    Observable<List<QAListInfoBean>> getQAQustion(String subject, Long maxId,String type);
    Observable<List<ExpertBean>> getTopicExperts(Long maxId, int topic_id);
}
