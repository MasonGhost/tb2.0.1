package com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */

public interface QASearchListContract {
    interface View extends ITSListView<QAListInfoBean, Presenter> {
        /**
         * @return 搜索的内容
         */
        String getSearchInput();
    }

    interface Presenter extends ITSListPresenter<QAListInfoBean> {

        List<QASearchHistoryBean> getFirstShowHistory();

        void cleaerAllSearchHistory();

        List<QASearchHistoryBean>  getAllSearchHistory();

        void deleteSearchHistory(QASearchHistoryBean qaSearchHistoryBean);
    }


}
