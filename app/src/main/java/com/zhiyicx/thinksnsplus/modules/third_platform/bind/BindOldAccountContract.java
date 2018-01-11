package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public interface BindOldAccountContract {

    interface View extends IBaseView<Presenter> {
        void showErrorTips(String message);

        void setLoginState(boolean b);

        void setLogining();

    }

    interface Presenter extends IBasePresenter {

        void bindAccount(ThridInfoBean thridInfoBean, String string, String string1);
    }

}
