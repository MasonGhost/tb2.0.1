package com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.IThirdPlatformRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public interface ChooseBindContract {

    interface View extends IBaseView<Presenter>{

    }

    interface Presenter extends IBasePresenter{

    }

    interface Repository extends IThirdPlatformRepository{

    }
}
