package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_DYNAMIC;
import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_INFO;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class StickTopRepsotory implements StickTopContract.Repository {

    DynamicClient mDynamicClient;
    InfoMainClient mInfoMainClient;



    @Inject
    public StickTopRepsotory(ServiceManager serviceManager) {
        mDynamicClient = serviceManager.getDynamicClient();
        mInfoMainClient = serviceManager.getInfoMainClient();
    }

    @Override
    public Observable<BaseJsonV2<Integer>> stickTop(String type, long parent_id, double amount, int day) {
        if (type.equals(TYPE_DYNAMIC)) {
            return mDynamicClient.stickTopDynamic(parent_id, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (type.equals(TYPE_INFO)) {
            return mInfoMainClient.stickTopInfo(parent_id, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return null;
        }

    }

    @Override
    public Observable<BaseJsonV2<Integer>> stickTop(String type, long parent_id, long child_id, double amount, int day) {
        if (type.equals(TYPE_DYNAMIC)) {
            return mDynamicClient.stickTopDynamicComment(parent_id, child_id, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (type.equals(TYPE_INFO)) {
            return mInfoMainClient.stickTopInfoComment(parent_id, child_id, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return null;
        }
    }
}
