package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.LiveClient;
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
    LiveRepository mLiveRepository;
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
                .flatMap(authBean1 -> {
                    mAuthRepository.saveAuthBean(authBean1);// 保存auth信息
                    return mLiveRepository.getLiveTicket()
                            .map(s -> {
                                authBean1.setLiveTicket(s);
                                return authBean1;
                            });
                })
                .flatMap(authBean -> mUserInfoRepository.getCurrentLoginUserInfo()
                        .map(userInfoBean -> {
                            authBean.setUser(userInfoBean);
                            authBean.setUser_id(userInfoBean.getUser_id());
                            mAuthRepository.saveAuthBean(authBean);
                            return authBean;
                        }));
    }

    @Override
    public Observable<AuthBean> registerByEmail(String email, String name, String vertifyCode, String password) {
        return mRegisterClient.register(null, email, name, password, RegisterClient.REGITER_TYPE_EMAIL, vertifyCode)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(authBean1 -> {
                    mAuthRepository.saveAuthBean(authBean1);// 保存auth信息
                    return mLiveRepository.getLiveTicket()
                            .map(s -> {
                                authBean1.setLiveTicket(s);
                                return authBean1;
                            });
                })
                .flatMap(authBean -> mUserInfoRepository.getCurrentLoginUserInfo()
                        .map(userInfoBean -> {
                            authBean.setUser(userInfoBean);
                            authBean.setUser_id(userInfoBean.getUser_id());
                            mAuthRepository.saveAuthBean(authBean);
                            return authBean;
                        }));
    }


}
