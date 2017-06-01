package com.zhiyicx.thinksnsplus.modules.dynamic.top;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Author Jliuer
 * @Date 2017/05/22/16:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface DynamicTopContract {

    interface View extends IBaseView<Presenter>{
        boolean insufficientBalance();
        void gotoRecharge();
    }

    interface Presenter extends IBasePresenter{
        float getBalance();
        void stickTop();
    }

    interface Repository {
        void stickTop();
    }
}
