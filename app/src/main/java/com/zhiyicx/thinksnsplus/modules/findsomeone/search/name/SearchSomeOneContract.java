package com.zhiyicx.thinksnsplus.modules.findsomeone.search.name;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */
public interface SearchSomeOneContract {

    interface View extends ITSListView<UserInfoBean, Presenter> {
        void upDateFollowFansState(int index);
    }

    interface Presenter extends FindSomeOneListContract.Presenter {

        void searchUser(String name);

        void getRecommentUser();
    }

}
