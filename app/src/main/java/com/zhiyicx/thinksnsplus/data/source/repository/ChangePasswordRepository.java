package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordContract;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class ChangePasswordRepository implements ChangePasswordContract.Repository {
    private CommonClient mCommonClient;
    private RegisterClient mRegisterClient;

    public ChangePasswordRepository(ServiceManager serviceManager) {
        mCommonClient = serviceManager.getCommonClient();
        mRegisterClient = serviceManager.getRegisterClient();
    }

    @Override
    public Observable<BaseJson<Boolean>> changePassword(String oldPassword, String newPassword) {
        return Observable.just(new BaseJson<Boolean>());
    }

}
