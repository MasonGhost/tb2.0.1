package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:38
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleDetailRepository extends BaseCircleRepository implements CircleDetailContract.Repository {

    @Inject
    public CircleDetailRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<CircleInfo> getCircleInfo(long circleId) {
        return mCircleClient.getCircleInfo(circleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
