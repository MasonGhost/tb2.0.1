package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/19
 * @contact email:648129313@qq.com
 */

public interface SelectDynamicTypeContract {

    interface View extends IBaseView<Presenter>{

    }

    interface Presenter extends IBaseTouristPresenter{
        boolean checkCertification();
        boolean isNeedPayTip();
        void savePayTip(boolean isNeed);
    }

    // 暂时没用，万一以后要搞点什么其他的 比如广告什么的
    interface Repository {

    }
}
