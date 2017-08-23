package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.register.RegisterContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
    UserInfoRepository mUserInfoRepository;
    @Inject
    AuthRepository mAuthRepository;

    @Inject
    public RegisterRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mCommonClient = serviceManager.getCommonClient();
        mRegisterClient = serviceManager.getRegisterClient();
    }

    @Override
    public Observable<AuthBean> registerByPhone(String phone, String name, String vertifyCode, String password) {
        return mRegisterClient.register(phone, null, name, password, RegisterClient.REGITER_TYPE_SMS, vertifyCode)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<AuthBean, Observable<AuthBean>>() {
                    @Override
                    public Observable<AuthBean> call(AuthBean authBean) {
                        mAuthRepository.saveAuthBean(authBean);
                        return mUserInfoRepository.getCurrentLoginUserInfo()
                                .map(userInfoBean -> {
                                    authBean.setUser(userInfoBean);
                                    authBean.setUser_id(userInfoBean.getUser_id());
                                    return authBean;
                                });
                    }
                });
    }

    @Override
    public Observable<AuthBean> registerByEmail(String email, String name, String vertifyCode, String password) {
        return mRegisterClient.register(null, email, name, password, RegisterClient.REGITER_TYPE_EMAIL, vertifyCode)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<AuthBean, Observable<AuthBean>>() {
                    @Override
                    public Observable<AuthBean> call(AuthBean authBean) {
                        mAuthRepository.saveAuthBean(authBean);
                        return mUserInfoRepository.getCurrentLoginUserInfo()
                                .map(userInfoBean -> {
                                    authBean.setUser(userInfoBean);
                                    authBean.setUser_id(userInfoBean.getUser_id());
                                    return authBean;
                                });
                    }
                });
    }

}
