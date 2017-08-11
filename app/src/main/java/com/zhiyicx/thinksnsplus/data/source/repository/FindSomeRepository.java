package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListContract;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */

public class FindSomeRepository implements FindSomeOneListContract.Repository {
    private FollowFansClient mFollowFansClient;
    @Inject
    protected UserInfoRepository mUserInfoRepository;
    @Inject
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public FindSomeRepository(ServiceManager serviceManager, Application context) {
        mFollowFansClient = serviceManager.getFollowFansClient();
    }

}
