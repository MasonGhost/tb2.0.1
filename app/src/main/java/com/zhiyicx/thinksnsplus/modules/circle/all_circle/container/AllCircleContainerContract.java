package com.zhiyicx.thinksnsplus.modules.circle.all_circle.container;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface AllCircleContainerContract {

    interface View extends IBaseView<Presenter> {
        void setCategroiesList(List<CircleTypeBean> circleTypeList);

        void setUserCertificationInfo(UserCertificationInfo userCertificationInfo);
    }

    interface Presenter extends IBaseTouristPresenter {
        void getCategroiesList(int limit, int offet);
        List<CircleTypeBean> getCircleTypesFormLocal();

        void checkCertification();

    }

    interface Repository extends IBaseCircleRepository {

    }
}
