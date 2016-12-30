package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.login.LoginContract;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public class LoginRepository implements LoginContract.Repository {
    private CommonClient mCommonClient;

    public LoginRepository(ServiceManager serviceManager) {
        mCommonClient = serviceManager.getCommonClient();
    }

    @Override
    public Observable<BaseJson<String>> login(String phone, String password) {
        return null;
    }
}
