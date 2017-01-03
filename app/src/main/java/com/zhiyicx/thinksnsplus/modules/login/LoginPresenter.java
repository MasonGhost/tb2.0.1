package com.zhiyicx.thinksnsplus.modules.login;

import android.support.annotation.MainThread;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.LoginBean;
import com.zhiyicx.thinksnsplus.data.source.repository.LoginRepository;
import com.zhiyicx.thinksnsplus.modules.register.RegisterContract;

import java.util.logging.Logger;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public class LoginPresenter extends BasePresenter<LoginContract.Repository, LoginContract.View> implements LoginContract.Presenter {

    @Inject
    public LoginPresenter(LoginContract.Repository repository, LoginContract.View rootView) {
        super(repository, rootView);
    }

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
            return;
        }
        mRepository.login(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TestAction<BaseJson<LoginBean>>() {
                    @Override
                    void testCall(BaseJson<LoginBean> integerBaseJson) {
                        LogUtils.i("login_success" + integerBaseJson);
                        Toast.makeText(mContext, "login_success" + integerBaseJson, Toast.LENGTH_SHORT).show();
                    }
                }, new TestAction<Throwable>() {
                    @Override
                    void testCall(Throwable e) {
                        LogUtils.i("login_failure" + e.getMessage());
                        Toast.makeText(mContext, "login_failure" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }
}
