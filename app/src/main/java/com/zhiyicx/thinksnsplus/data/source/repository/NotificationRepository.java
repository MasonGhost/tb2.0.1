package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.data.source.repository.i.INotificationRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe 通知相关
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public class NotificationRepository implements INotificationRepository {

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
