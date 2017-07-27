package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class ChangePasswordRepository implements ChangePasswordContract.Repository {
    private PasswordClient mPasswordClient;
    @Inject
    public ChangePasswordRepository(ServiceManager serviceManager) {
        mPasswordClient = serviceManager.getPasswordClient();
    }

    @Override
    public Observable<BaseJson<CacheBean>> changePassword(String oldPassword, String newPassword) {
        return mPasswordClient.changePassword("success", oldPassword, newPassword)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CacheBean> changePasswordV2(String oldPassword, String newPassword) {
        return mPasswordClient.changePasswordV2(oldPassword, newPassword, newPassword)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
