package com.zhiyicx.thinksnsplus.modules.q_a.mine.follow;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBasePublishQuestionRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public interface MyFollowContract {

    interface View extends ITSListView<BaseListBean, Presenter>{
        String getType();
        void updateTopicFollowState(QATopicBean qaTopicBean);
    }

    interface Presenter extends ITSListPresenter<BaseListBean>{
        void handleTopicFollowState(int position, QATopicBean qaTopicBean);
    }

}
