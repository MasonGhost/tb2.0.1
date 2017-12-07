package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.main.CircleMainContract;
import com.zhiyicx.thinksnsplus.modules.circle.search.container.CircleSearchContainerContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jliuer
 * @Date 2017/11/14/11:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainRepository extends BaseCircleRepository implements CircleMainContract.Repository,CircleSearchContainerContract.Repository {

    @Inject
    public CircleMainRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<List<CircleInfo>> getRecommendCircle(int limit,int offet) {
        return mCircleClient.getRecommendCircle(limit,offet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
