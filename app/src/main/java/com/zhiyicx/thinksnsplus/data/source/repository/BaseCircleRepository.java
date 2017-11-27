package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BaseCircleRepository implements IBaseCircleRepository {

    protected CircleClient mCircleClient;

    @Inject
    public BaseCircleRepository(ServiceManager serviceManager) {
        mCircleClient = serviceManager.getCircleClient();
    }

    @Override
    public Observable<List<CircleTypeBean>> getCategroiesList(int limit, int offet) {
        return mCircleClient.getCategroiesList(TSListFragment.DEFAULT_ONE_PAGE_SIZE, offet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
