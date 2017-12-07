package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.CircleListContract;
import com.zhiyicx.thinksnsplus.modules.circle.mine.joined.BaseCircleListContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleListRepository extends BaseCircleRepository implements CircleListContract.Repository, BaseCircleListContract.Repository {

    @Inject
    public CircleListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<List<CircleInfo>> getCircleList(long categoryId, long maxId) {
        return mCircleClient.getCircleList(categoryId, TSListFragment.DEFAULT_ONE_PAGE_SIZE, (int) maxId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
