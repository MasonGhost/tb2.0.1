package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IThirdPlatformRepository;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public interface CompleteAccountContract {

    interface View extends IBaseView<Presenter>{
        void showErrorTips(String message);
    }

    interface Presenter extends IBasePresenter{

    }

    interface Repository extends IThirdPlatformRepository{

    }
}
