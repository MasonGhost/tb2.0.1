package com.zhiyicx.thinksnsplus.modules.chat.edit.owner;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * Created by Catherine on 2018/1/22.
 */

public interface EditGroupOwnerContract {
    interface View extends TSListFragment<Presenter, UserInfoBean> {
    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {
    }

    interface Repository {
    }
}
