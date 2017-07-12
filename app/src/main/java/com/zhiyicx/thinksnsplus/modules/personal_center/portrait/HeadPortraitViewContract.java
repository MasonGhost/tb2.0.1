package com.zhiyicx.thinksnsplus.modules.personal_center.portrait;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @author Catherine
 * @describe 需求还没定，可能有更换头像之类的吧 开心就好
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */

public interface HeadPortraitViewContract {

    interface View extends IBaseView<Presenter> {

    }

    interface Presenter extends IBasePresenter {
        UserInfoBean getCurrentUser(long user_id);
    }

    interface Repository {

    }
}
