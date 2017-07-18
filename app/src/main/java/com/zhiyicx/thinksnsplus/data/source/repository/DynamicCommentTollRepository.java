package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentToll;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment.DynamicCommentTollContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicCommentTollRepository implements DynamicCommentTollContract.Repository {

    DynamicClient mDynamicClient;


    @Inject
    public DynamicCommentTollRepository(ServiceManager serviceManager) {
        mDynamicClient = serviceManager.getDynamicClient();
    }

    @Override
    public Observable<DynamicCommentToll> tollDynamicComment(Long feed_id, int amount) {
        return mDynamicClient.setDynamicCommentToll(feed_id, amount)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
