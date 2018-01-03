package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface LocationSearchContract {

    interface View extends ITSListView<LocationBean, Presenter> {
    }

    interface Presenter extends ITSListPresenter<LocationBean> {

        void searchLocation(String name);

    }

}
