package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IPasswordRepository;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordContract;

import java.util.HashMap;
import java.util.Map;

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

public class PasswordRepository extends VertifyCodeRepository implements IPasswordRepository{

    private PasswordClient mPasswordClient;
    @Inject
    public PasswordRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mPasswordClient = serviceManager.getPasswordClient();
    }


    @Override
    public Observable<CacheBean> findPasswordV2(String phone, String vertifyCode, String newPassword) {
        return  mPasswordClient.findPasswordV2(phone, null, vertifyCode, PasswordClient.REGITER_TYPE_SMS, newPassword)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CacheBean> findPasswordByEmail(String email, String verifyCode, String newPassword) {
        return mPasswordClient.findPasswordV2( null, email, verifyCode, PasswordClient.REGITER_TYPE_EMAIL, newPassword)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> changePasswordV2(String oldPassword, String newPassword) {
        Map<String, String> data = new HashMap<>();
        data.put("old_password", oldPassword);
        data.put("password", newPassword);
        data.put("password_confirmation", newPassword);

        return mPasswordClient.changePasswordV2(data)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
