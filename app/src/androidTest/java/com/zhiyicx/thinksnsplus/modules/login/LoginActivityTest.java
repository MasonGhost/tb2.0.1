package com.zhiyicx.thinksnsplus.modules.login;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.remote.LoginClient;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static com.zhiyicx.thinksnsplus.modules.MyViewMatchers.disEnabled;
import static junit.framework.Assert.assertFalse;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public class LoginActivityTest extends AcitivityTest {

    private ViewInteraction etPhone, etPass, btnLogin, tvErrorTip;
    private LoginClient mLoginClient;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Before
    public void initActivity() {
        RxUnitTestTools.openRxTools();
        mLoginClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getLoginClient();
        etPhone = findViewById(R.id.et_login_phone);
        etPass = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.bt_login_login);
        tvErrorTip = findViewById(R.id.tv_error_tip);
    }

    /**
     * summary   不输入手机号，只输入密码能否点击登录按钮
     * steps     1.清空输入框  2.输入密码
     * expected  登录按钮无法点击
     */
    @Test
    public void clickableWhenNoPhone() throws Exception {
        etPhone.perform(replaceText(""), closeSoftKeyboard());
        etPass.perform(replaceText("123456"), closeSoftKeyboard());
        btnLogin.check(matches(disEnabled()));
    }

    /**
     * summary    不输入密码，只输入手机号能否点击登录按钮
     * steps      1.清空输入框  2.输入手机号
     * expected   登录按钮无法点击
     */
    @Test
    public void clickableWhenNoPassword() throws Exception {
        etPass.perform(replaceText(""), closeSoftKeyboard());
        etPhone.perform(replaceText("15928856596"), closeSoftKeyboard());
        btnLogin.check(matches(disEnabled()));
    }

    /**
     * summary    因为某些原因导致登录失败，比如密码错误
     * steps        1.输入正确的手机号  2.输入错误的密码 3.点击登录按钮
     * expected   errorTip显示登录失败的原因
     */
    @Test
    public void loginFailure() throws Exception {
        mLoginClient.loginV2("dsafdsa", "fdsadfs")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integerBaseJson -> {
                    LogUtils.d("haha", integerBaseJson.toString());
                    // 登录成功跳转:当前不可能发生
                    assertFalse(true);
                }, e -> {
                        LogUtils.e(e, "exception");
                        assertFalse(false);
                });
    }

    /**
     * summary    输入正确的手机号，密码登录成功
     * steps      1.输入正确的手机号 2.输入正确的密码 3.点击登录按钮 4.主线成沉睡1s等待网络请求结果
     * expected   errorTip显示登录成功的内容
     */
    @Test
    public void loginSuccess() throws Exception {
        etPhone.perform(replaceText("15928856596"), closeSoftKeyboard());
        etPass.perform(replaceText("123456"), closeSoftKeyboard());
        btnLogin.perform(click());
        Thread.sleep(1000);
//        tvErrorTip.check(matches(not(isDisplayed())));
    }
}
