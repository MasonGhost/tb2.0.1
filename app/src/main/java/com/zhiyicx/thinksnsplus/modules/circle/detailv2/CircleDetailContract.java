package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDetail;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleDetailContract {
    interface View extends ITSListView<CirclePostListBean, Presenter> {
        long getCircleId();
        void allDataReady(CircleZipBean circleZipBean);
    }

    interface Presenter extends ITSListPresenter<CirclePostListBean> {
    }

    interface Repository extends IBaseCircleRepository {
        Observable<CircleInfoDetail> getCircleInfoDetail(long circleId);
    }
}
