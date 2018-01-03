package com.zhiyicx.thinksnsplus.modules.settings.bind;

import android.os.CountDownTimer;
import android.text.TextUtils;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.PasswordRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.VertifyCodeRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

import static com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter.S_TO_MS_SPACING;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountBindPresenter extends BasePresenter<AccountBindContract.View>
        implements AccountBindContract.Presenter {
    public static final int DEFAULT_DELAY_CLOSE_TIME = 2_000;
    public static final int SNS_TIME = 60 * S_TO_MS_SPACING; // 发送短信间隔时间，单位 ms

    private int mTimeOut = SNS_TIME;

    @Inject
    VertifyCodeRepository mVertifyCodeRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    PasswordRepository mChangePasswordRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    CountDownTimer timer = new CountDownTimer(mTimeOut, S_TO_MS_SPACING) {

        @Override
        public void onTick(long millisUntilFinished) {
            mRootView.setVerifyCodeBtText(millisUntilFinished / S_TO_MS_SPACING + mContext.getString(R.string.seconds));//显示倒数的秒速
        }

        @Override
        public void onFinish() {
            mRootView.setVerifyCodeBtEnabled(true);//恢复初始化状态
            mRootView.setVerifyCodeBtText(mContext.getString(R.string.send_vertify_code));
        }
    };


    @Inject
    public AccountBindPresenter(AccountBindContract.View rootView) {
        super(rootView);
    }

    /**
     * 获取验证码
     *
     * @param phone  电话号码
     * @param isBind true, 解绑
     */
    @Override
    public void getVertifyCode(String phone, boolean isBind) {
        if (checkPhone(phone)) {
            return;
        }
        mRootView.setVerifyCodeBtEnabled(false);
        mRootView.setVerifyCodeLoading(true);
        Subscription getVertifySub = (isBind ? mVertifyCodeRepository.getMemberVertifyCode(phone) : mVertifyCodeRepository.getNonMemberVertifyCode
                (phone))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.hideLoading();//隐藏loading
                        timer.start();//开始倒计时
                        mRootView.setVerifyCodeLoading(false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                        mRootView.setVerifyCodeBtEnabled(true);
                        mRootView.setVerifyCodeLoading(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                        mRootView.setVerifyCodeBtEnabled(true);
                        mRootView.setVerifyCodeLoading(false);
                    }

                });
        // 代表检测成功
        mRootView.showMessage("");
        addSubscrebe(getVertifySub);
    }

    /**
     * @param email
     * @param isBind true, 解绑
     */
    @Override
    public void getVerifyCodeByEmail(String email, boolean isBind) {
        if (checkEmail(email)) {
            return;
        }
        mRootView.setVerifyCodeBtEnabled(false);
        mRootView.setVerifyCodeLoading(true);

        Subscription getVerifySub = (isBind ? mVertifyCodeRepository.getMemberVerifyCodeByEmail(email) : mVertifyCodeRepository
                .getNonMemberVerifyCodeByEmail(email))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.hideLoading();//隐藏loading
                        timer.start();//开始倒计时
                        mRootView.setVerifyCodeLoading(false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                        mRootView.setVerifyCodeBtEnabled(true);
                        mRootView.setVerifyCodeLoading(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                        mRootView.setVerifyCodeBtEnabled(true);
                        mRootView.setVerifyCodeLoading(false);
                    }

                });
        // 代表检测成功
        mRootView.showMessage("");
        addSubscrebe(getVerifySub);
    }

    /**
     * @param password
     * @param verifyCode
     * @param isPhone
     */
    @Override
    public void unBindPhoneOrEmail(String password, String verifyCode, boolean isPhone) {
        if (checkPasswordLength(password)) {
            return;
        }
        if (checkVertifyLength(verifyCode)) {
            return;
        }
        mRootView.setSureBtEnabled(false);
        Observable<Object> objectObservable;
        if (isPhone) {
            objectObservable = mUserInfoRepository.deletePhone(password, verifyCode);

        } else {
            objectObservable = mUserInfoRepository.deleteEmail(password, verifyCode);
        }
        Subscription subscribe = objectObservable
                .doAfterTerminate(() -> mRootView.setSureBtEnabled(true))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
                        if (isPhone) {
                            userInfoBean.setPhone(null);
                        } else {
                            userInfoBean.setEmail(null);
                        }
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.unbind_success));
                        Observable.timer(DEFAULT_DELAY_CLOSE_TIME, TimeUnit.MILLISECONDS)
                                .subscribe(aLong -> mRootView.unBindPhoneOrEmailSuccess(isPhone));

                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));

                    }
                });
        addSubscrebe(subscribe);
    }

    /**
     * @param pasword
     * @param surepassword
     * @param phone
     * @param email
     * @param verifyCode
     * @param isPhone
     */
    @Override
    public void bindPhoneOrEmail(String pasword, String surepassword, String phone, String email, String verifyCode, boolean isPhone) {
        // 现在绑定都不需要密码了
//        if (!pasword.equals(surepassword)) {
//            mRootView.showMessage(mContext.getString(R.string.password_diffrent));
//            return;
//        }
//        if (checkPasswordLength(pasword)) {
//            return;
//        }
        if (isPhone && checkPhone(phone)) {
            return;
        }
        if (checkVertifyLength(verifyCode)) {
            return;
        }
        mRootView.setSureBtEnabled(false);

        Subscription subscribe = mUserInfoRepository.updatePhoneOrEmail(isPhone ? phone : null, isPhone ? null : email, verifyCode)
                .doAfterTerminate(() -> mRootView.setSureBtEnabled(true))
                .flatMap(new Func1<Object, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(Object o) {
                        if (TextUtils.isEmpty(surepassword)) {
                            return Observable.just(o);
                        } else {
                            return mChangePasswordRepository.changePasswordV2(null, surepassword);
                        }
                    }
                })
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
                        if (isPhone) {
                            userInfoBean.setPhone(phone);
                        } else {
                            userInfoBean.setEmail(email);
                        }
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.bind_success));
                        Observable.timer(DEFAULT_DELAY_CLOSE_TIME, TimeUnit.MILLISECONDS)
                                .subscribe(aLong -> mRootView.BindPhoneOrEmailSuccess(isPhone));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));

                    }
                });

        addSubscrebe(subscribe);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

}
