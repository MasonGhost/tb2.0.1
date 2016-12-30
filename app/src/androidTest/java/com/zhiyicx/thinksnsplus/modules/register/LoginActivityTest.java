package com.zhiyicx.thinksnsplus.modules.register;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    private ViewInteraction etPhone, etPass, btnLogin;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Before
    public void initActivity() {
        etPhone = onView(withId(R.id.et_login_phone));
        etPass = onView(withId(R.id.et_login_password));
        btnLogin = onView(withId(R.id.bt_login_login));
    }

    /**
     * 不输入手机号，登录按钮无法点击
     */
    @Test
    public void clickableWhenNoPhone() {
        etPhone.perform(clearText());
        etPass.perform(clearText());
        etPhone.perform(replaceText("空寂很哈dsafas"));
        etPass.perform(replaceText("fdasfdsafdas"), closeSoftKeyboard());
        btnLogin.check(matches(isClickable()));
    }

    /**
     * 不输入密码，登录按钮无法点击
     */
    @Test
    public void clickableWhenNoPassword() {

    }

    /**
     * 不输入手机号登陆
     */
    @Test
    public void notInputPhone() {

    }

    /**
     * 不输入密码登陆
     */
    @Test
    public void notInputPassword() {

    }

    /**
     * 输入合法手机号和不合法密码
     */
    @Test
    public void inputIllegalPassword() {

    }

    /**
     * 输入不合法手机号和合法密码
     */
    @Test
    public void inputIllegalPhone() {

    }

    /**
     * 输入合法手机号和合法密码
     */
    @Test
    public void inputRight() {

    }

    /**
     * 密码大小写测试
     */
    @Test
    public void changePasseordLowerUpper() {

    }
}
