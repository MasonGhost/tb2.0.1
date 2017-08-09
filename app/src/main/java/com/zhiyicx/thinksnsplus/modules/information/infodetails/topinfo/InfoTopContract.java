package com.zhiyicx.thinksnsplus.modules.information.infodetails.topinfo;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/05/22/16:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoTopContract {

    interface View extends IBaseView<Presenter> {
        boolean insufficientBalance();

        void gotoRecharge();

        void initStickTopInstructionsPop();

        int getTopDyas();

        double getInputMoney();

        void topSuccess();
    }

    interface Presenter extends IBasePresenter {
        double getBalance();

        void stickTop(long news_id);
    }

    interface Repository {
        Observable<BaseJsonV2<Integer>> stickTop(long news_id, double amount, int day);
    }
}
