package com.zhiyicx.thinksnsplus.modules.tb.privacy;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Author Jliuer
 * @Date 2018/03/01/15:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface PrivacyContract {
    interface View extends IBaseView<Presenter>{
        void onChangeRankStatus(boolean rank);

        void onGetRankStatus(int rankStatus);
    }
    interface Presenter extends IBaseTouristPresenter{
        void changeRankStatus(boolean rank);

        void getRankStatus();
    }
}
