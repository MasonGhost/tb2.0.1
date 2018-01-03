package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class QATopiclistModule {

    QATopicListConstact.View mView;

    public QATopiclistModule(QATopicListConstact.View view) {
        mView = view;
    }

    @Provides
    QATopicListConstact.View provideQATopicListConstactView() {
        return mView;
    }

}
