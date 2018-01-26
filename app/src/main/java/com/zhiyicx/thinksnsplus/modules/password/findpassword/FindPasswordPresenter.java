package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import android.os.CountDownTimer;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.PasswordRepository;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class FindPasswordPresenter extends BasePresenter<FindPasswordContract.View> implements FindPasswordContract.Presenter {
    public static final int S_TO_MS_SPACING = 1000; // s 和 ms 的比例
    public static final int SNS_TIME = 60 * S_TO_MS_SPACING; // 发送短信间隔时间，单位 ms

    private int mTimeOut = SNS_TIME;

    @Inject
    PasswordRepository mPasswordRepository;

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
    public FindPasswordPresenter( FindPasswordContract.View rootView) {
        super( rootView);
    }


    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 找回密码
     *
     * @param phone       电话号码
     * @param vertifyCode 验证码
     * @param newPassword 新密码
     */
    @Override
    public void findPassword(String phone, String vertifyCode, String newPassword) {

        if (checkPhone(phone)) {
            return;
        }
        if (checkVertifyLength(vertifyCode)) {
            return;
        }
        if (checkPasswordLength(newPassword)) {
            return;
        }
        mRootView.setSureBtEnabled(false);
        Subscription findPasswordSub = mPasswordRepository.findPasswordV2(phone, vertifyCode, newPassword)
                .subscribe(new BaseSubscribeForV2<CacheBean>() {
                    @Override
                    protected void onSuccess(CacheBean data) {
                        mRootView.finsh();
                        mRootView.setSureBtEnabled(true);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                        mRootView.setSureBtEnabled(true);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                        mRootView.setSureBtEnabled(true);
                    }
                });
        // 代表检测成功
        mRootView.showMessage("");
        addSubscrebe(findPasswordSub);
    }

    @Override
    public void findPasswordByEmail(String email, String vertifyCode, String newPassword) {
        if (checkEmail(email)) {
            return;
        }
        if (checkVertifyLength(vertifyCode)) {
            return;
        }
        if (checkPasswordLength(newPassword)) {
            return;
        }
        mRootView.setSureBtEnabled(false);
        Subscription findPasswordSub = mPasswordRepository.findPasswordByEmail(email, vertifyCode, newPassword)
                .subscribe(new BaseSubscribeForV2<CacheBean>() {
                    @Override
                    protected void onSuccess(CacheBean data) {
                        mRootView.showMessage(mContext.getString(R.string.find_password_success));
                        mRootView.finsh();
                        mRootView.setSureBtEnabled(true);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                        mRootView.setSureBtEnabled(true);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                        mRootView.setSureBtEnabled(true);
                    }
                });
        // 代表检测成功
        mRootView.showMessage("");
        addSubscrebe(findPasswordSub);
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
        mRootView.setVertifyCodeLoading(true);
        Subscription getVertifySub = mPasswordRepository.getMemberVertifyCode(phone)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.hideLoading();//隐藏loading
                        timer.start();//开始倒计时
                        mRootView.setVertifyCodeLoading(false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                        mRootView.setVertifyCodeBtEnabled(true);
                        mRootView.setVertifyCodeLoading(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                        mRootView.setVertifyCodeBtEnabled(true);
                        mRootView.setVertifyCodeLoading(false);
                    }

                });
        // 代表检测成功
        mRootView.showMessage("");
        addSubscrebe(getVertifySub);
    }

    @Override
    public void getVerifyCodeByEmail(String email) {
        if (checkEmail(email)){
            return;
        }
        mRootView.setVertifyCodeBtEnabled(false);
        mRootView.setVertifyCodeLoading(true);
        Subscription getVerifySub = mPasswordRepository.getMemberVerifyCodeByEmail(email)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.hideLoading();//隐藏loading
                        timer.start();//开始倒计时
                        mRootView.setVertifyCodeLoading(false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                        mRootView.setVertifyCodeBtEnabled(true);
                        mRootView.setVertifyCodeLoading(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                        mRootView.setVertifyCodeBtEnabled(true);
                        mRootView.setVertifyCodeLoading(false);
                    }

                });
        // 代表检测成功
        mRootView.showMessage("");
        addSubscrebe(getVerifySub);
    }

    /**
     * 检测验证码码是否正确
     */
    private boolean checkVertifyLength(String vertifyCode) {
        if (vertifyCode.length() < mContext.getResources().getInteger(R.integer.vertiry_code_min_lenght)) {
            mRootView.showMessage(mContext.getString(R.string.vertify_code_input_hint));
            return true;
        }
        return false;
    }

    /**
     * 检测手机号码是否正确
     */
    private boolean checkPhone(String phone) {
        if (!RegexUtils.isMobileExact(phone)) {
            mRootView.showMessage(mContext.getString(R.string.phone_number_toast_hint));
            return true;
        }
        return false;
    }

    /**
     * 检测邮箱地址
     *
     * @param email 地址
     */
    private boolean checkEmail(String email) {
        if (!RegexUtils.isEmail(email)) {
            mRootView.showMessage(mContext.getString(R.string.email_address_toast_hint));
            return true;
        }
        return false;
    }

    /**
     * 检查密码是否是最小长度
     */
    private boolean checkPasswordLength(String password) {
        if (password.length() < mContext.getResources().getInteger(R.integer.password_min_length)) {
            mRootView.showMessage(mContext.getString(R.string.password_toast_hint));
            return true;
        }
        return false;
    }


}
