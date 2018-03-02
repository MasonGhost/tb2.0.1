package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface ContributionListContract {
    interface View extends ITSListView<ContributionData,Presenter>{
        String getType();
    }
    interface Presenter extends ITSListPresenter<ContributionData>{}
}
