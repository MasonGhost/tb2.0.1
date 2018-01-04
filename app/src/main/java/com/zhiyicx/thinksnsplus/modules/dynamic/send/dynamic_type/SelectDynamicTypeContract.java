package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/19
 * @contact email:648129313@qq.com
 */

public interface SelectDynamicTypeContract {

    interface View extends IBaseView<Presenter>{
        void setUserCertificationInfo(UserCertificationInfo userCertificationInfo);
    }

    interface Presenter extends IBaseTouristPresenter{
        void checkCertification();
        boolean isNeedPayTip();
        void savePayTip(boolean isNeed);
    }

}
