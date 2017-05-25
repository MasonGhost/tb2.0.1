package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordContract;

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

public class FindPasswordRepository extends VertifyCodeRepository implements FindPasswordContract.Repository {

    private PasswordClient mPasswordClient;
    @Inject
    public FindPasswordRepository(ServiceManager serviceManager, Application application) {
        super(serviceManager,application);
        mPasswordClient = serviceManager.getPasswordClient();
    }

    @Override
    public Observable<BaseJson<CacheBean>> findPassword(String phone, String vertifyCode, String newPassword) {
        return mPasswordClient.findPassword("success", phone, vertifyCode, newPassword)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
