package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.source.remote.LoginClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.login.LoginContract;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public class LoginRepository implements LoginContract.Repository {
    private LoginClient mLoginClient;
    @Inject
    Application mContext;

    @Inject
    public LoginRepository(ServiceManager serviceManager) {
        mLoginClient = serviceManager.getLoginClient();
    }

    @Override
    public Observable<BaseJson<AuthBean>> login(Context context, final String phone, final String password) {
       /* if(cacheImp==null){
            cacheImp = new CacheImp<>(new AuthBeanGreenDaoImpl(context));
        }
        return cacheImp.load(1483098241l, new NetWorkCache<AuthBean>() {
            @Override
            public Observable<BaseJson<AuthBean>> get(Long key) {
            }
        });*/
        return mLoginClient.login("success", phone, password, DeviceUtils.getIMEI(mContext));
    }
    @Override
    public Observable<AuthBean> loginV2(final String account, final String password) {
       /* if(cacheImp==null){
            cacheImp = new CacheImp<>(new AuthBeanGreenDaoImpl(context));
        }
        return cacheImp.load(1483098241l, new NetWorkCache<AuthBean>() {
            @Override
            public Observable<BaseJson<AuthBean>> get(Long key) {
            }
        });*/
        return mLoginClient.loginV2(account, password);
    }
}
