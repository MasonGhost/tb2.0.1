package com.zhiyicx.thinksnsplus.modules.settings.bind;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public interface AccountBindContract {

    interface View extends IBaseView<Presenter>{

        void setSureBtEnabled(boolean isEnable);

        void setVerifyCodeBtEnabled(boolean isEnable);

        void setVerifyCodeLoading(boolean isEnable);

        void setVerifyCodeBtText(String text);

    }

    interface Presenter extends IBasePresenter {

    }

    interface Repository{

    }
}
