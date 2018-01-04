package com.zhiyicx.thinksnsplus.modules.circle.manager.earning;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/12/12/11:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleEarningContract {
    interface View extends IBaseView<Presenter> {
        void permissionResult(List<String> permission);
    }

    interface Presenter extends IBaseTouristPresenter {
        void setCirclePermissions(long circleId, List<String> permissions);
    }

}
