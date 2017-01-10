package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.password.PasswordContract;
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

public class PasswordRepository implements PasswordContract.Repository {
    private CommonClient mCommonClient;
    private RegisterClient mRegisterClient;

    public PasswordRepository(ServiceManager serviceManager) {
        mCommonClient = serviceManager.getCommonClient();
        mRegisterClient = serviceManager.getRegisterClient();
    }

}
