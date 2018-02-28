package com.zhiyicx.thinksnsplus.modules.rank.tb;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:42
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface RankListContract {

    interface View extends ITSListView<RankData,Presenter>{

    }
    interface Presenter extends ITSListPresenter<RankData>{

    }
}
