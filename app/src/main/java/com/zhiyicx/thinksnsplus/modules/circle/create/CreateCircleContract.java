package com.zhiyicx.thinksnsplus.modules.circle.create;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CreateCircleContract {
    interface View extends IBaseView<Presenter> {
        void setCircleInfo(CircleInfo data);
    }

    interface Presenter extends IBaseTouristPresenter {
        void createCircle(CreateCircleBean createCircleBean);
        void updateCircle(CreateCircleBean createCircleBean);
        String getCircleCategoryName(int category);
    }

    interface Repository extends IBaseCircleRepository {
    }
}
