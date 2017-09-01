package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist.NotificationContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public class NotificationRepository implements NotificationContract.Repository{

    protected UserInfoClient mUserInfoClient;

    @Inject
    public NotificationRepository(ServiceManager serviceManager) {
        mUserInfoClient = serviceManager.getUserInfoClient();
    }

    @Override
    public Observable<List<TSPNotificationBean>> getNotificationList(int size) {
        return mUserInfoClient.getNotificationList(null, ApiConfig.NOTIFICATION_TYPE_ALL, TSListFragment.DEFAULT_PAGE_SIZE, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
