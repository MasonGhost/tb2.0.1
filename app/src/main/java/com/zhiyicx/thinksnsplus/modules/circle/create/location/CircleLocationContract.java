package com.zhiyicx.thinksnsplus.modules.circle.create.location;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/11/21/16:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleLocationContract {

    interface View extends ITSListView<LocationBean, Presenter> {
    }

    interface Presenter extends ITSListPresenter<LocationBean> {
        void searchLocation(String name);
    }

}
