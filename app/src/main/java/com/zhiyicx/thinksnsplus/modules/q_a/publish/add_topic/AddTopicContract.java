package com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

public interface AddTopicContract {

    interface View extends ITSListView<QATopicBean, Presenter> {
        void updateSuccess(QAListInfoBean listInfoBean);
    }

    interface Presenter extends ITSListPresenter<QATopicBean> {
        void saveQuestion(QAPublishBean qestion);
        void updateQuestion(QAPublishBean qaPublishBean);
        QAPublishBean getDraftQuestion(long qestion_mark);
       void requestNetData(String name, Long maxId, Long follow,boolean isLoadMore);
    }

}
