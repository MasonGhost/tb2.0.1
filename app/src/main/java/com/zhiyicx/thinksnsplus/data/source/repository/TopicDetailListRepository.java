package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list.TopicDetailListContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public class TopicDetailListRepository extends BaseQARepository implements TopicDetailListContract.Repository {

    @Inject
    public TopicDetailListRepository(ServiceManager manager) {
        super(manager);
    }
}
