package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import com.amap.api.services.core.LatLonPoint;
import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListContract;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/11
 * @Contact master.jungle68@gmail.com
 */
public interface FindSomeOneContainerContract {

    interface View extends IBaseView<Presenter> {
    }

    interface Presenter extends IBasePresenter {
        void updateUseLocation(LatLonPoint latLonPoint);
    }
}
