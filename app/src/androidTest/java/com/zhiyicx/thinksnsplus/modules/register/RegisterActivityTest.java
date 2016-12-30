package com.zhiyicx.thinksnsplus.modules.register;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.thinksnsplus.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.zhiyicx.thinksnsplus.modules.MyViewMatchers.disEnabled;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/23
 * @Contact master.jungle68@gmail.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterActivityTest {
    private static final String USER_NAME = "jungle";
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule(RegisterActivity.class);

    @Before

    /**
     * summary                       不输入用户名
     * steps                         不输入昵称点击注册
     * expected                      按钮颜色不亮，无法点击
     *
     * @throws Exception
     */
    @Test
    public void inputUsernameCheck() throws Exception {
        onView(withId(R.id.et_regist_nickname)).perform(typeText(USER_NAME), closeSoftKeyboard());
//        onView(withId(R.id.et_regist_phone)).perform(typeText("15694005009"), closeSoftKeyboard());
//        onView(withId(R.id.et_regist_vertify_code)).perform(typeText("1234"));
//        onView(withId(R.id.et_regist_password)).perform(typeText("fdagiasdg"),closeSoftKeyboard());
        onView(withId(R.id.bt_regist_regist)).perform(click()).check(matches(disEnabled()
        ));
    }

    /**
     * summary                       昵称字符长度是否有限制(最长、最短)
     * steps                         1. 昵称输入文字若干
     * 2. 填写正确手机号、验证码、密码
     * 3. 点击注册
     * expected                     字符为空时，不能点击“注册”；昵称过短，提示应为3-8个字符；
     * 昵称达到8位后，不能再输入
     *
     * @throws Exception
     */
    @Test
    public void usernameLengthCheck() throws Exception {
        //获取 Fragment 中的 text
        ViewInteraction fragmentText = onView(withId(R.id.et_regist_nickname));
        //验证 text 不存在
//        fragmentText.check(doesNotExist());
        //点击按钮显示 fragment
        fragmentText.check(matches(withText("Hello blank fragment")));
    }
}