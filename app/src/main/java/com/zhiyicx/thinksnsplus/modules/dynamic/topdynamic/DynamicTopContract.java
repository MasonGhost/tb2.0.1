package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Author Jliuer
 * @Date 2017/05/22/16:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface DynamicTopContract {

    interface View extends IBaseView<Presenter> {
        boolean insufficientBalance();

        void gotoRecharge();

        void initStickTopInstructionsPop();

        int getTopDyas();

        double getInputMoney();

        void topSuccess();
    }

    interface Presenter extends IBaseTouristPresenter {
        /**
         *
         * @return 积分余额
         */
        long getBalance();

        void stickTop(long feed_id);
    }

}
