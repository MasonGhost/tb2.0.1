package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordContract;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class FindPasswordRepository implements FindPasswordContract.Repository {
    private CommonClient mCommonClient;
    private PasswordClient mPasswordClient;

    public FindPasswordRepository(ServiceManager serviceManager) {
        mCommonClient = serviceManager.getCommonClient();
        mPasswordClient = serviceManager.getPasswordClient();
    }
    @Override
    public Observable<BaseJson<CacheBean>> getVertifyCode(String phone, String type) {
        return mCommonClient.getVertifyCode("success", phone, type)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJson<CacheBean>> findPassword(String phone, String vertifyCode, String newPassword) {
        return mPasswordClient.findPassword("success", phone, vertifyCode, newPassword)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
