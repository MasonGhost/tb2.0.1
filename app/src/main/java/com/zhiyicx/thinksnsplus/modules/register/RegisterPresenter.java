package com.zhiyicx.thinksnsplus.modules.register;

import android.os.CountDownTimer;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
    public static final int USERNAME_MIN_LENGTH = 2; // 用户名最小长度
    public static final int PASSWORD_MIN_LENGTH = 6; // 密码最小长度

    private int mTimeOut = SNS_TIME;

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

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void getVertifyCode(String phone) {
        if (!RegexUtils.isMobileExact(phone)) {
            mRootView.showMessage(mContext.getString(R.string.phone_number_toast_hint));
            return;
        }
        mRepository.getVertifyCode(phone)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<String>>() {
                    @Override
                    public void call(BaseJson<String> json) {
//                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                        mRootView.hideLoading();//隐藏loading
                        timer.start();//开始倒计时
                        mRootView.setVertifyCodeBtEnabled(false);
                        mRootView.showMessage(json.getData());
//                        } else {
//                            mRootView.showMessage(json.getMessage());
//                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        // 代表检测成功
        mRootView.showMessage("");
    }

    @Override
    public void register(String name, String phone, String vertifyCode, String password) {
        if (!checkUsername(name)) {
            return;
        }
        if (!checkPassword(password)) {
            return;
        }
        mRepository.register(phone, name, vertifyCode, password)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<String>>() {
                    @Override
                    public void call(BaseJson<String> json) {
//                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                        mRootView.hideLoading();//隐藏loading
                        timer.start();//开始倒计时
                        mRootView.setVertifyCodeBtEnabled(false);
                        mRootView.showMessage(json.getData());
//                        } else {
//                            mRootView.showMessage(json.getMessage());
//                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        // 代表检测成功
        mRootView.showMessage("");
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        timer.cancel();
    }

    /**
     * 检查用户名是否小于最小长度,不能以数字开头
     *
     * @param name
     * @return
     */
    private boolean checkUsername(String name) {
        if (name.length() < USERNAME_MIN_LENGTH) {
            mRootView.showMessage(mContext.getString(R.string.username_toast_hint));
            return false;
        }
        if (RegexUtils.isUsernameNoNumberStart(name)) {// 数字开头
            mRootView.showMessage(mContext.getString(R.string.username_toast_not_number_start_hint));
            return false;
        }
        if (!RegexUtils.isUsername(name)) {// 用户名只能包含数字、字母和下划线
            mRootView.showMessage(mContext.getString(R.string.username_toast_not_symbol_hint));
            return false;
        }
        return true;
    }

    /**
     * 检查密码是否是最小长度
     *
     * @param password
     * @return
     */
    private boolean checkPassword(String password) {
        if (password.length() < PASSWORD_MIN_LENGTH) {
            mRootView.showMessage(mContext.getString(R.string.password_toast_hint));
            return false;
        }
        return true;
    }
}
