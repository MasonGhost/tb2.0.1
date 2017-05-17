package com.zhiyicx.thinksnsplus.modules.login;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class LoginPresenter extends AppBasePresenter<LoginContract.Repository, LoginContract.View> implements LoginContract.Presenter {

    @Inject
    public LoginPresenter(LoginContract.Repository repository, LoginContract.View rootView) {
        super(repository, rootView);
    }

    @Inject
    AuthRepository mAuthRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    /**
     * 将Presenter从传入fragment
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void login(String phone, String password) {

        if (!RegexUtils.isMobileExact(phone)) {
            // 不符合手机号格式
            mRootView.showErrorTips(mContext.getString(R.string.phone_number_toast_hint));
            return;
        }
        mRootView.setLogining();
        Subscription subscription = mRepository.login(mContext, phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<AuthBean>, Observable<BaseJson<List<UserInfoBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<UserInfoBean>>> call(BaseJson<AuthBean> authBeanBaseJson) {
                        if (authBeanBaseJson.isStatus()) {
                            // 登录成功跳转
                            mAuthRepository.saveAuthBean(authBeanBaseJson.getData());// 保存auth信息
                            // IM 登录 需要 token ,所以需要先保存登录信息
                            handleIMLogin();
                            // 获取用户信息
                            List<Object> userids = new ArrayList<>();
                            userids.add(Long.valueOf(authBeanBaseJson.getData().getUser_id()));
                            return mUserInfoRepository.getUserInfo(userids);
                        }
                        BaseJson<List<UserInfoBean>> userInfobean = new BaseJson<>();
                        userInfobean.setStatus(authBeanBaseJson.isStatus());
                        userInfobean.setCode(authBeanBaseJson.getCode());
                        userInfobean.setMessage(authBeanBaseJson.getMessage());
                        return Observable.just(userInfobean);
                    }
                })
                .subscribe(new BaseSubscribe<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        mRootView.setLoginState(true);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        // 登录失败
                        mRootView.setLoginState(false);
                        mRootView.showErrorTips(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e(throwable, "login_error" + throwable.getMessage());
                        mRootView.showErrorTips(mContext.getString(R.string.err_net_not_work));
                        mRootView.setLoginState(false);
                    }
                });
        addSubscrebe(subscription);
    }

    private void handleIMLogin() {
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.GET_IM_INFO));
    }

}
