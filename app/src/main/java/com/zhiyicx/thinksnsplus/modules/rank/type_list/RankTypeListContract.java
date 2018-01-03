package com.zhiyicx.thinksnsplus.modules.rank.type_list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseRankRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public interface RankTypeListContract {

    interface View extends ITSListView<UserInfoBean, Presenter>{
        String getRankType();
    }

    interface Presenter extends ITSListPresenter<UserInfoBean>{
        void handleFollowState(UserInfoBean userInfoBean);
    }

}
