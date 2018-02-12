package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentToll;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface DynamicCommentTopContract {

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
         * @return 积分余额
         */
        long getBalance();
        void topDynamicComment(long feed_id,long comment_id, double amount, int day);
    }

}
