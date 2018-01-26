package com.zhiyicx.thinksnsplus.modules.certification.send;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public interface SendCertificationContract {

    interface View extends IBaseView<Presenter> {
        void updateSendState(boolean isSending, boolean isSuccess, String message);
    }

    interface Presenter extends IBasePresenter {
        void sendCertification(SendCertificationBean bean);
        SendCertificationBean checkUpdateData(SendCertificationBean bean);
    }

}
