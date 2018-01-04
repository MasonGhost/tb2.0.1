package com.zhiyicx.thinksnsplus.modules.certification.input;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */

public interface CertificationInputContract {

    interface View extends IBaseView<Presenter>{
        void showErrorTips(String error);
        void sendSuccess();
    }

    interface Presenter extends IBasePresenter{

    }

}
