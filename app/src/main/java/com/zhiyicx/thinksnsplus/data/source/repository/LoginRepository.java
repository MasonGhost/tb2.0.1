package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.baseproject.cache.CacheImp;
import com.zhiyicx.baseproject.cache.NetWorkCache;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.source.local.AuthBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.LoginClient;
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
    private CacheImp<AuthBean> cacheImp;
    private Context mContext;

    public LoginRepository(ServiceManager serviceManager, Context context) {
        mLoginClient = serviceManager.getLoginClient();
        mContext = context;
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
}
