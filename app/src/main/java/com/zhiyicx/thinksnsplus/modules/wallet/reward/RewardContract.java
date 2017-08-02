package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IRewardRepository;
import com.zhiyicx.tspay.TSPayClient;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public interface RewardContract {

    interface View extends IBaseView<Presenter> {


    }

    interface Repository extends IRewardRepository {

    }

    interface Presenter extends IBaseTouristPresenter {

    }
}
