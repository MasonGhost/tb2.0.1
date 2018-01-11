package com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IThirdPlatformRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public interface ChooseBindContract {

    interface View extends IBaseView<Presenter>{

    }

    interface Presenter extends IBaseTouristPresenter{
    }

}
