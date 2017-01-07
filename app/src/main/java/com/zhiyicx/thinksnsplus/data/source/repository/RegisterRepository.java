package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.register.RegisterContract;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class RegisterRepository implements RegisterContract.Repository {
    private CommonClient mCommonClient;


    public RegisterRepository(ServiceManager serviceManager) {
        super();
        mCommonClient = serviceManager.getCommonClient();

    }

    @Override
    public Observable<BaseJson<String>> getVertifyCode(String phone) {
        return Observable.just(new BaseJson<String>());
    }

    @Override
    public Observable<BaseJson<String>> register(String phone, String name, String vertifyCode, String password) {
        return Observable.just(new BaseJson<String>());
    }
}
