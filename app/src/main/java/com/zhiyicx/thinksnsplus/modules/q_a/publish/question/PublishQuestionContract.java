package com.zhiyicx.thinksnsplus.modules.q_a.publish.question;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import retrofit2.http.Query;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public interface PublishQuestionContract {

    interface View extends ITSListView<QAListInfoBean, Presenter> {
    }

    interface Presenter extends ITSListPresenter<QAListInfoBean> {
        void saveQuestion(QAPublishBean qestion);
        QAPublishBean getDraftQuestion(long qestion_mark);
        void deleteQuestion(QAPublishBean qestion);
        void requestNetData(String subject, Long maxId,String type,boolean isLoadMore);

        /**
         * 检查问答配置
         */
        void checkQuestionConfig();
    }

}
