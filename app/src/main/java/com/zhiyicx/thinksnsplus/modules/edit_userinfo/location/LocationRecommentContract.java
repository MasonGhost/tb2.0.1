package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */

public interface LocationRecommentContract {
    interface View extends IBaseView<Presenter> {

        void updateHotCity(List<LocationBean> data);
    }

    interface Repository {

    }

    interface Presenter extends IBasePresenter {

        void getHotCity();
    }
}
