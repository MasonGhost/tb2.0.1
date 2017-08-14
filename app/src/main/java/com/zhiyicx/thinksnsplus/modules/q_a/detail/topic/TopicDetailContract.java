package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */

public interface TopicDetailContract {

    interface View extends ITSListView<QAListInfoBean, Presenter>{
        void setTopicDetail(QATopicBean qaTopicBean);
        Long getTopicId();
        String getCurrentType();
        QATopicBean getCurrentTopicBean();
        void updateFollowState();
    }

    interface Presenter extends ITSListPresenter<QAListInfoBean>{
        void getTopicDetail(String topic_id);
        void handleTopicFollowState(String topic_id, boolean isFollow);
    }

    interface Repository extends IBasePublishQuestionRepository {
        Observable<QATopicBean> getTopicDetail(String topic_id);
    }
}
