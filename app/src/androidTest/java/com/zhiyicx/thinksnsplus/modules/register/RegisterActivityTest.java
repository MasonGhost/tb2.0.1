package com.zhiyicx.thinksnsplus.modules.register;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.thinksnsplus.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.zhiyicx.thinksnsplus.modules.MyViewMatchers.disEnabled;
import static com.zhiyicx.thinksnsplus.modules.MyViewMatchers.isDisappear;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/23
 * @Contact master.jungle68@gmail.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterActivityTest {
    private static final String USER_NAME = "七夜";
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule(RegisterActivity.class);


    /**
     * summary                       不输入用户名
     * steps                         不输入用户名点击注册
     * expected                      按钮颜色不亮，无法点击
     *
     * @throws Exception
     */
    @Test
    public void inputUsernameCheck() throws Exception {
        onView(withId(R.id.et_regist_username)).perform(replaceText(USER_NAME), closeSoftKeyboard());
        onView(withId(R.id.bt_regist_regist)).perform(click()).check(matches(disEnabled()));
    }

    /**
     * summary                       用户名字符长度是否有限制(最长、最短)
     * steps                         1. 用户名输入文字若干
     *                               2. 填写正确手机号、验证码、密码
     *                               3. 点击注册
     * expected                      字符为空时，不能点击“注册”
     * 用户名达到8位后，不能再输入
     *
     * @throws Exception
     */
    @Test
    public void usernameNotNull() throws Exception {
        onView(withId(R.id.et_regist_phone)).perform(typeText("15694005009"));
        onView(withId(R.id.et_regist_vertify_code)).perform(typeText("1234"));
        onView(withId(R.id.et_regist_password)).perform(typeText("fdagiasdg"));
        // 字符为空时，不能点击“注册”；
        onView(withId(R.id.et_regist_username)).perform(typeText(""));
        onView(withId(R.id.bt_regist_regist)).perform(click()).check(matches(disEnabled()));

    }


    /**
     * summary                       用户名字符长度是否有限制(最长、最短)
     * steps                         1. 用户名输入文字若干
     *                               2. 填写正确手机号、验证码、密码
     *                               3. 点击注册
     * expected                      用户名过短，提示应为 2-8 个字符；
     *                                  用户名达到8位后，不能再输入
     *
     * @throws Exception
     */
    @Test
    public void usernameMinLength() throws Exception {
        onView(withId(R.id.et_regist_phone)).perform(typeText("15694005009"));
        onView(withId(R.id.et_regist_vertify_code)).perform(typeText("1234"));
        onView(withId(R.id.et_regist_password)).perform(typeText("fdagiasdg"));
        // 用户名过短，提示应为 2-8 个字符
        onView(withId(R.id.et_regist_username)).perform(replaceText("你"));
        onView(withId(R.id.bt_regist_regist)).perform(click());
        onView(withId(R.id.tv_error_tip)).check(matches(withText(mActivityRule.getActivity().getString(R.string.username_toast_hint))));

    }

    /**
     * summary                       用户名字符长度是否有限制(最长、最短)
     * steps                         1. 用户名输入文字若干
     *                               2. 填写正确手机号、验证码、密码
     *                               3. 点击注册
     * expected                      2-8 个字符正确注册
     *
     *
     * @throws Exception
     */
    @Test
    public void usernameRightLength() throws Exception {
        onView(withId(R.id.et_regist_phone)).perform(typeText("15694005009"));
        onView(withId(R.id.et_regist_vertify_code)).perform(typeText("1234"));
        onView(withId(R.id.et_regist_password)).perform(typeText("fdagiasdg"));
        // 用户名过短，提示应为 3-8 个字符
        onView(withId(R.id.et_regist_username)).perform(replaceText(USER_NAME));
        onView(withId(R.id.bt_regist_regist)).check(matches(isEnabled())).perform(click());
        onView(withId(R.id.tv_error_tip)).check(matches(isDisappear()));
    }

}