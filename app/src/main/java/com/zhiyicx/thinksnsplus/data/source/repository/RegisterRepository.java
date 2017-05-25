package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.register.RegisterContract;

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

public class RegisterRepository extends VertifyCodeRepository implements RegisterContract.Repository {
    private RegisterClient mRegisterClient;

    @Inject
    public RegisterRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mCommonClient = serviceManager.getCommonClient();
        mRegisterClient = serviceManager.getRegisterClient();
    }

    @Override
    public Observable<BaseJson<AuthBean>> register(String phone, String name, String vertifyCode, String password) {
        return mRegisterClient.register("success", phone, name, vertifyCode, password, DeviceUtils.getIMEI(mContext))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
