package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IStickTopRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_DYNAMIC;
import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_INFO;
import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_POST;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class StickTopRepsotory implements IStickTopRepository {

    DynamicClient mDynamicClient;
    InfoMainClient mInfoMainClient;
    CircleClient mCircleClient;


    @Inject
    public StickTopRepsotory(ServiceManager serviceManager) {
        mDynamicClient = serviceManager.getDynamicClient();
        mInfoMainClient = serviceManager.getInfoMainClient();
        mCircleClient = serviceManager.getCircleClient();
    }

    @Override
    public Observable<BaseJsonV2<Integer>> stickTop(String type, long parentId, double amount, int day) {
        if (type.equals(TYPE_DYNAMIC)) {
            return mDynamicClient.stickTopDynamic(parentId, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (type.equals(TYPE_INFO)) {
            return mInfoMainClient.stickTopInfo(parentId, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (type.equals(TYPE_POST)) {
            return mCircleClient.stickTopPost(parentId, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return null;
        }

    }

    @Override
    public Observable<BaseJsonV2<Integer>> stickTop(String type, long parentId, long childId, double amount, int day) {
        if (type.equals(TYPE_DYNAMIC)) {
            return mDynamicClient.stickTopDynamicComment(parentId, childId, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (type.equals(TYPE_INFO)) {
            return mInfoMainClient.stickTopInfoComment(parentId, childId, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (type.equals(TYPE_POST)) {
            return mCircleClient.stickTopPostComment(parentId, childId, (long) amount, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return null;
        }
    }
}
