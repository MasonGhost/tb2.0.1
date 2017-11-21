package com.zhiyicx.thinksnsplus.modules.circle.create;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CreateCircleContract {
    interface View extends IBaseView<Presenter>{}
    interface Presenter extends IBaseTouristPresenter{}
    interface Repository extends IBaseCircleRepository{}
}
