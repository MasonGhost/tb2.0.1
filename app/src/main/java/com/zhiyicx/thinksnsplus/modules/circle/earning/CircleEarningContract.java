package com.zhiyicx.thinksnsplus.modules.circle.earning;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

/**
 * @Author Jliuer
 * @Date 2017/12/12/11:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleEarningContract {
    interface View extends IBaseView<Presenter> {
    }

    interface Presenter extends IBaseTouristPresenter {
    }

    interface Repository extends IBaseCircleRepository {
    }
}
