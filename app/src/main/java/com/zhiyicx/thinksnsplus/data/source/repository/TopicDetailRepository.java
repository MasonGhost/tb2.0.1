package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailContract;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */

public class TopicDetailRepository extends BaseQARepository implements TopicDetailContract.Repository{

    @Inject
    public TopicDetailRepository(ServiceManager manager) {
        super(manager);
    }

    @Override
    public Observable<QATopicBean> getTopicDetail(String topic_id) {
        return mQAClient.getTopicDetail(topic_id);
    }
}
