package com.zhiyicx.thinksnsplus.modules.chat.select;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public interface SelectFriendsContract {

    interface View extends ITSListView<UserInfoBean, Presenter> {

    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {

    }

    interface Repository extends IBaseFriendsRepository{

    }
}
