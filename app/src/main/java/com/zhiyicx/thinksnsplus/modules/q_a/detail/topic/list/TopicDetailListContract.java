package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public interface TopicDetailListContract {

    interface View extends ITSListView<QAListInfoBean, Presenter> {
        String getCurrentType();

        Long getTopicId();

        void showDeleteSuccess();
    }

    interface Presenter extends ITSListPresenter<QAListInfoBean> {
        void payForOnlook(long answer_id, int position);
    }

    interface Repository extends IBasePublishQuestionRepository {
    }
}
