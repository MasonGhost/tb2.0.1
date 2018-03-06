package com.zhiyicx.thinksnsplus.modules.tb.search;

import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListContract;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */
public interface SearchMechanismUserContract {

    interface View extends ITSListView<UserInfoBean, Presenter> {
        void upDateFollowFansState(int index);
    }

    interface Presenter extends FindSomeOneListContract.Presenter {

        void searchUser(String name);


        List<SearchHistoryBean> getFirstShowHistory();

        List<SearchHistoryBean> getAllSearchHistory();

        void cleaerAllSearchHistory();

        void deleteSearchHistory(SearchHistoryBean searchHistoryBean);

        void refreshUserFollow();

    }

}
