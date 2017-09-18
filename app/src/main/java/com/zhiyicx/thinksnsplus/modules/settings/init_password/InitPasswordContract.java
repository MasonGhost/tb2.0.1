package com.zhiyicx.thinksnsplus.modules.settings.init_password;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/18
 * @contact email:648129313@qq.com
 */

public interface InitPasswordContract {

    interface View extends IBaseView<Presenter> {
        void initPasswordResult(boolean isSuccess);
    }

    interface Presenter extends IBasePresenter {
        void initPassword(String password, String confirm_password);
    }

    interface Repository {

    }
}
