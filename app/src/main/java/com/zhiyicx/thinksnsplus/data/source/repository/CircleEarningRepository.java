package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.manager.earning.CircleEarningContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/12/12/13:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleEarningRepository extends BaseCircleRepository implements CircleEarningContract.Repository {

    @Inject
    public CircleEarningRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<List<CircleEarningListBean>> getCircleEarningList(Long circleId, Long start, Long end,
                                                                        Long after, Long limit, String type) {

        return mCircleClient.getCircleEarningList(circleId, start, end, after, limit, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
