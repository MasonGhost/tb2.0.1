package com.zhiyicx.thinksnsplus.modules.tb.detail;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/23
 * @Contact master.jungle68@gmail.com
 */
public interface MerchainMessageListContract {
    interface View extends ITSListView<ContributionData,Presenter>{
    }
    interface Presenter extends ITSListPresenter<ContributionData>{}
}
