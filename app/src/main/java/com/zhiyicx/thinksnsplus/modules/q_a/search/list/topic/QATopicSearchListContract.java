package com.zhiyicx.thinksnsplus.modules.q_a.search.list.topic;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */

public interface QATopicSearchListContract {
    interface View extends ITSListView<QATopicBean, Presenter> {
        /**
         * @return 搜索的内容
         */
        String getSearchInput();
    }

    interface Presenter extends ITSListPresenter<QATopicBean> {

    }

    interface Repository {


    }

}
