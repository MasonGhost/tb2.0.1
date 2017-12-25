package com.zhiyicx.thinksnsplus.modules.circle.create;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.circle.CreateCircleBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CreateCircleContract {
    interface View extends IBaseView<Presenter> {
        void setCircleInfo(CircleInfo data);

        /**
         * 用于圈子协议
         */
        void setCircleRule(String rule);
    }

    interface Presenter extends IBaseTouristPresenter {
        void createCircle(CreateCircleBean createCircleBean);
        void updateCircle(CreateCircleBean createCircleBean);
        String getCircleCategoryName(int category);
        void getRule();
    }

    interface Repository extends IBaseCircleRepository {
    }
}
