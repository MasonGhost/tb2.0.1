package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface QATopicListConstact {

    interface View extends ITSListView<QATopicBean,Presenter>{
        String getType();
    }

    interface Presenter extends ITSListPresenter<QATopicBean>{
        void requestNetData(String type, Long maxId,boolean isLoadMore);// 关注
        void requestNetData(String name, Long maxId, Long follow,boolean isLoadMore);// 全部
        void handleTopicFollowState(int position,String topic_id, boolean isFollow);

        List<QASearchHistoryBean> getFirstShowHistory();

        void cleaerAllSearchHistory();

        List<QASearchHistoryBean>  getAllSearchHistory();

        void deleteSearchHistory(QASearchHistoryBean qaSearchHistoryBean);
    }

}
