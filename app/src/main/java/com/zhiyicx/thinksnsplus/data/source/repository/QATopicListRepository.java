package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist.QATopicListConstact;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QATopicListRepository implements QATopicListConstact.Repository {

    @Inject
    public QATopicListRepository(ServiceManager serviceManager) {
    }
}
