package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic.DynamicTopContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicTopRepsotory implements DynamicTopContract.Repository {

    DynamicClient mDynamicClient;

    @Inject
    public DynamicTopRepsotory(ServiceManager serviceManager) {
        mDynamicClient = serviceManager.getDynamicClient();
    }

    @Override
    public Observable<BaseJsonV2<Integer>> stickTop(long feed_id, int amount, int day) {
        return mDynamicClient.stickTopDynamic(feed_id, amount, day)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
