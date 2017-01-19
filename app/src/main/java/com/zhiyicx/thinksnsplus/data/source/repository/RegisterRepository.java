package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.register.RegisterContract;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class RegisterRepository implements RegisterContract.Repository {
    private CommonClient mCommonClient;
    private RegisterClient mRegisterClient;
    private Context mContext;

    public RegisterRepository(ServiceManager serviceManager, Context context) {
        mCommonClient = serviceManager.getCommonClient();
        mRegisterClient = serviceManager.getRegisterClient();
        mContext = context;
    }

    @Override
    public Observable<BaseJson<CacheBean>> getVertifyCode(String phone, String type) {
        return mCommonClient.getVertifyCode("success", phone, type)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJson<AuthBean>> register(String phone, String name, String vertifyCode, String password) {
        return mRegisterClient.register("success", phone, name, vertifyCode, password, DeviceUtils.getIMEI(mContext))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean saveAuthBean(AuthBean authBean) {

        return false;
    }
}
