package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic.AddTopicContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public class AddTopicRepository extends BasePublishQuestionRepository implements AddTopicContract.Repository{

    @Inject
    public AddTopicRepository(ServiceManager manager) {
        super(manager);
    }
}
