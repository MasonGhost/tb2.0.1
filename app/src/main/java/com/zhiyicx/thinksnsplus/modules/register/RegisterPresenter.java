package com.zhiyicx.thinksnsplus.modules.register;

import android.os.CountDownTimer;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.repository.IAuthRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class RegisterPresenter extends BasePresenter<RegisterContract.Repository, RegisterContract.View> implements RegisterContract.Presenter {

    public static final int S_TO_MS_SPACING = 1000; // s 和 ms 的比例
    public static final int SNS_TIME = 60 * S_TO_MS_SPACING; // 发送短信间隔时间，单位 ms

    private int mTimeOut = SNS_TIME;

    @Inject
    IAuthRepository mAuthRepository;

    CountDownTimer timer = new CountDownTimer(mTimeOut, S_TO_MS_SPACING) {

        @Override
        public void onTick(long millisUntilFinished) {
            mRootView.setVertifyCodeBtText(millisUntilFinished / S_TO_MS_SPACING + mContext.getString(R.string.seconds));//显示倒数的秒速
        }

        @Override
        public void onFinish() {
            mRootView.setVertifyCodeBtEnabled(true);//恢复初始化状态
            mRootView.setVertifyCodeBtText(mContext.getString(R.string.send_vertify_code));
        }
    };

    @Inject
    public RegisterPresenter(RegisterContract.Repository repository, RegisterContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    /**
     * 获取验证码
     *
     * @param phone 电话号码
     */
    @Override
    public void getVertifyCode(String phone) {
        if (checkPhone(phone)) {
            return;
        }
        mRootView.setVertifyCodeBtEnabled(false);
        mRootView.setVertifyCodeLoadin(true);
        Subscription getVertifySub = mRepository.getVertifyCode(phone, CommonClient.VERTIFY_CODE_TYPE_REGISTER)
                .subscribe(new BaseSubscribe<CacheBean>() {
                    @Override
                    protected void onSuccess(CacheBean data) {
                        mRootView.hideLoading();//隐藏loading
                        timer.start();//开始倒计时
                        mRootView.setVertifyCodeLoadin(false);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.showMessage(message);
                        mRootView.setVertifyCodeBtEnabled(true);
                        mRootView.setVertifyCodeLoadin(false);
                    }

                    @Override
                    protected void onException(Throwable e) {
                        handleException(e);
                        mRootView.setVertifyCodeBtEnabled(true);
                        mRootView.setVertifyCodeLoadin(false);
                    }
                });

        // 代表检测成功
        mRootView.showMessage("");
        addSubscrebe(getVertifySub);
    }

    /**
     * 注册
     *
     * @param name        用户名
     * @param phone       电话号码
     * @param vertifyCode 验证码
     * @param password    密码
     */
    @Override
    public void register(String name, String phone, String vertifyCode, String password) {
        if (checkUsername(name)) {
            return;
        }
        if (checkPhone(phone)) {
            return;
        }
        if (checkVertifyLength(vertifyCode)) {
            return;
        }
        if (checkPasswordLength(password)) {
            return;
        }
        mRootView.setRegisterBtEnabled(false);
        Subscription registerSub = mRepository.register(phone, name, vertifyCode, password)
                .subscribe(new BaseSubscribe<AuthBean>() {
                    @Override
                    public void onSuccess(AuthBean data) {
                        mRootView.setRegisterBtEnabled(true);
                        mAuthRepository.saveAuthBean(data);// 保存登录认证信息
                        // 获取用户信息
                        getUserInfo(data);
                        // IM 登录 需要 token ,所以需要先保存登录信息
                        handleIMLogin();
                        mRootView.goHome();

                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.setRegisterBtEnabled(true);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        handleException(throwable);
                        mRootView.setRegisterBtEnabled(true);
                    }
                });

        // 代表检测成功
        mRootView.showMessage("");
        addSubscrebe(registerSub);
    }

    private void handleIMLogin() {
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.GET_IM_INFO));
    }

    private void getUserInfo(AuthBean data) {
        HashMap<String, Object> userInfoParams = new HashMap<>();
        userInfoParams.put("user", data.getUser_id());
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.GET_USER_INFO, userInfoParams));
    }


    /**
     * 错误处理
     *
     * @param throwable 错误内容
     */
    private void handleException(Throwable throwable) {
        throwable.printStackTrace();
        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
    }

    /**
     * 检测验证码码是否正确
     *
     * @param vertifyCode
     * @return
     */
    private boolean checkVertifyLength(String vertifyCode) {
        if (vertifyCode.length() != mContext.getResources().getInteger(R.integer.vertiry_code_lenght)) {
            mRootView.showMessage(mContext.getString(R.string.vertify_code_input_hint));
            return true;
        }
        return false;
    }

    /**
     * 检测手机号码是否正确
     *
     * @param phone
     * @return
     */
    private boolean checkPhone(String phone) {
        if (!RegexUtils.isMobileExact(phone)) {
            mRootView.showMessage(mContext.getString(R.string.phone_number_toast_hint));
            return true;
        }
        return false;
    }

    /**
     * 检查用户名是否小于最小长度,不能以数字开头
     *
     * @param name
     * @return
     */
    private boolean checkUsername(String name) {
        if (!RegexUtils.isUsernameLength(name, mContext.getResources().getInteger(R.integer.username_min_length))) {
            mRootView.showMessage(mContext.getString(R.string.username_toast_hint));
            return true;
        }
        if (RegexUtils.isUsernameNoNumberStart(name)) {// 数字开头
            mRootView.showMessage(mContext.getString(R.string.username_toast_not_number_start_hint));
            return true;
        }
        if (!RegexUtils.isUsername(name)) {// 用户名只能包含数字、字母和下划线
            mRootView.showMessage(mContext.getString(R.string.username_toast_not_symbol_hint));
            return true;
        }
        return false;
    }

    /**
     * 检查密码是否是最小长度
     *
     * @param password
     * @return
     */
    private boolean checkPasswordLength(String password) {
        if (password.length() < mContext.getResources().getInteger(R.integer.password_min_length)) {
            mRootView.showMessage(mContext.getString(R.string.password_toast_hint));
            return true;
        }
        return false;
    }
}
