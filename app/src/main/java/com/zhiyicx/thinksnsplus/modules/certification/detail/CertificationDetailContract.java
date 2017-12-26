package com.zhiyicx.thinksnsplus.modules.certification.detail;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public interface CertificationDetailContract {

    interface View extends IBaseView<Presenter>{
        void setCertificationInfo(UserCertificationInfo info);
    }

    interface Presenter extends IBasePresenter{
        void getCertificationInfo();
    }

}
