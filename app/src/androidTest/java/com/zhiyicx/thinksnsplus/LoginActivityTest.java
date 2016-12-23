package com.zhiyicx.thinksnsplus;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author LiuChao
 * @describe 登陆功能的测试用例
 * @date 2016/12/23
 * @contact email:450127106@qq.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    /**
     * 不输入手机号登陆
     */
    @Test
    public void notInputPhone() {
        onView(withId(R.id.bt_login_login)).perform(click());
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
