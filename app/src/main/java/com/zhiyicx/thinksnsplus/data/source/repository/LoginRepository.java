package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.baseproject.cache.CacheImp;
import com.zhiyicx.baseproject.cache.NetWorkCache;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.LoginBean;
import com.zhiyicx.thinksnsplus.data.source.remote.LoginClient;
import com.zhiyicx.thinksnsplus.data.source.local.LoginBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.login.LoginContract;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public class LoginRepository implements LoginContract.Repository {
    private LoginClient mLoginClient;
    private CacheImp<LoginBean> cacheImp;

    public LoginRepository(ServiceManager serviceManager) {
        mLoginClient = serviceManager.getLoginClient();
    }

    @Override
    public Observable<BaseJson<LoginBean>> login(Context context, final String phone, final String password) {
        if(cacheImp==null){
            cacheImp = new CacheImp<>(new LoginBeanGreenDaoImpl(context));
        }
        return cacheImp.load("1483098241", new NetWorkCache<LoginBean>() {
            @Override
            public Observable<BaseJson<LoginBean>> get(String key) {
                return mLoginClient.login("success", phone, password, "dfsafds").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        });
    }
}
