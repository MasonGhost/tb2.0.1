package com.zhiyicx.thinksnsplus.modules.home.mine.friends.search;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */

public interface SearchFriendsContract {

    interface View extends ITSListView<UserInfoBean, Presenter> {
        /**
         * 获取关键词
         *
         * @return string
         */
        String getKeyWord();
    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {
    }

    interface Repository extends IBaseFriendsRepository {
    }
}
