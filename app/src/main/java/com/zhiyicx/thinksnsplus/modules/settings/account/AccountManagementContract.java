package com.zhiyicx.thinksnsplus.modules.settings.account;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public interface AccountManagementContract {

    interface View extends IBaseView<Presenter>{

        void updateBindStatus(List<String> data, UserInfoBean singleDataFromCache);
    }

    interface Presenter extends IBasePresenter{

        void getBindSocialAcounts();
    }

    interface Repository{

    }
}
