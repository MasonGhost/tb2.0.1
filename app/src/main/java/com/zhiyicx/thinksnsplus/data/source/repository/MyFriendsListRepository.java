package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.MyFriendsListContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */

public class MyFriendsListRepository implements MyFriendsListContract.Repository{

    FollowFansClient mClient;
    @Inject
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public MyFriendsListRepository(ServiceManager manager) {
        mClient = manager.getFollowFansClient();
    }

    @Override
    public Observable<List<UserInfoBean>> getUserFriendsList(long maxId) {
        return mClient.getUserFriendsList(maxId, TSListFragment.DEFAULT_PAGE_SIZE)
                .observeOn(Schedulers.io())
                .map(userInfoBeen -> {
                    // 保存用户信息
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBeen);
                    return userInfoBeen;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
