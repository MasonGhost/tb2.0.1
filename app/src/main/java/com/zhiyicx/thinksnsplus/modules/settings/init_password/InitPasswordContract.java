package com.zhiyicx.thinksnsplus.modules.settings.init_password;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/18
 * @contact email:648129313@qq.com
 */

public interface InitPasswordContract {

    interface View extends IBaseView<Presenter> {
        void initPasswordResult(boolean isSuccess);
        UserInfoBean getCurrentUser();
    }

    interface Presenter extends IBasePresenter {
        void initPassword(String password, String confirm_password);
    }

}
