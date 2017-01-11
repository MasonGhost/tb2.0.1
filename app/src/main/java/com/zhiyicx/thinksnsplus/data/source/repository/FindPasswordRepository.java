package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordContract;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordContract;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */

public class FindPasswordRepository implements FindPasswordContract.Repository {
    private CommonClient mCommonClient;
    private RegisterClient mRegisterClient;

    public FindPasswordRepository(ServiceManager serviceManager) {
        mCommonClient = serviceManager.getCommonClient();
        mRegisterClient = serviceManager.getRegisterClient();
    }
    @Override
    public Observable<BaseJson<CacheBean>> getVertifyCode(String phone, String type) {
        return Observable.just(new BaseJson<CacheBean>());
    }
    @Override
    public Observable<BaseJson<Boolean>> findPassword(String phone, String vertifyCode) {
        return Observable.just(new BaseJson<Boolean>());
    }
}
