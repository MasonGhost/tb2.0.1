package com.zhiyicx.thinksnsplus.modules.register;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static com.zhiyicx.thinksnsplus.modules.MyViewMatchers.isUnClickable;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public class LoginActivityTest extends AcitivityTest {

    private ViewInteraction etPhone, etPass, btnLogin;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Before
    public void initActivity() {
        etPhone = findViewById(R.id.et_login_phone);
        etPass = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.bt_login_login);
    }

    /**
     * summary   不输入手机号，只输入密码能否点击登录按钮
     * steps     1.清空输入框  2.输入密码
     * expected  登录按钮无法点击
     */
    @Test
    public void clickableWhenNoPhone() throws Exception {
        clearEditText(etPhone, etPass);
        etPass.perform(replaceText("123456"), closeSoftKeyboard());
        btnLogin.check(matches(isUnClickable()));
    }

    /**
     * summary    不输入密码，只输入手机号能否点击登录按钮
     * steps      1.清空输入框  2.输入手机号
     * expected   登录按钮无法点击
     */
    @Test
    public void clickableWhenNoPassword() throws Exception {
        clearEditText(etPhone, etPass);
        etPhone.perform(replaceText("15928856596"), closeSoftKeyboard());
        btnLogin.check(matches(isUnClickable()));
    }

    /**
     * summary    输入错误手机号，正确的密码，提示
     * steps      1.清空输入框 2.输入错误的手机号 3.输入正确的密码
     * expected   提示手机号错误
     */
    @Test
    public void wrongPhone() throws Exception {

    }

    /**
     * summary    输入正确手机号，错误的密码，提示
     * steps      1.清空输入框 2.输入正确的手机号 3.输入错误的密码
     * expected   提示密码错误
     */
    @Test
    public void wrongPassword() throws Exception {

    }
}
