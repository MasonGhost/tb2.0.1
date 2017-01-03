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
    private static final String USER_PHONE = "15694005009";
    private static final String USER_NAME = "七夜26";
    private static final String TEST_USER_NAME = "啊哈";
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
     * 2. 填写正确手机号、验证码、密码
     * 3. 点击注册
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
     * 2. 填写正确手机号、验证码、密码
     * 3. 点击注册
     * expected                      用户名过短，提示应为 2-8 个字符；
     * 用户名达到8位后，不能再输入
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
     * 2. 填写正确手机号、验证码、密码
     * 3. 点击注册
     * expected                      用户名可以包含数字
     *
     * @throws Exception
     */
    @Test
    public void usernameContainerNumber() throws Exception {
        onView(withId(R.id.et_regist_phone)).perform(typeText("15694005009"));
        onView(withId(R.id.et_regist_vertify_code)).perform(typeText("1234"));
        onView(withId(R.id.et_regist_password)).perform(typeText("fdagiasdg"));
        // 用户名可以包含数字
        onView(withId(R.id.et_regist_username)).perform(replaceText(USER_NAME));
        onView(withId(R.id.bt_regist_regist)).check(matches(isEnabled())).perform(click());
        onView(withId(R.id.tv_error_tip)).check(matches(isDisappear()));
    }

    /**
     * summary                       用户名字符长度是否有限制(最长、最短)
     * steps                         1. 用户名输入文字若干
     * 2. 填写正确手机号、验证码、密码
     * 3. 点击注册
     * expected                      2-8 个字符正确注册
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
    /**
     * summary                      用户名是否可以输入特殊字符
     *
     * steps                        1.输入用户名“测￥%”
                                    2.填写手机号，验证码，密码
                                    3.点击注册

     * expected                   .提示“用户名只能包含数字、字母和下划线”
     *
     * @throws Exception
     */
    @Test
    public void usernameRightSymbol() throws Exception {
        onView(withId(R.id.et_regist_phone)).perform(typeText(USER_PHONE));
        onView(withId(R.id.et_regist_vertify_code)).perform(typeText("1234"));
        onView(withId(R.id.et_regist_password)).perform(typeText("fdagiasdg"));
        // 用户名过短，提示应为 3-8 个字符
        onView(withId(R.id.et_regist_username)).perform(replaceText("测￥%"));
        onView(withId(R.id.bt_regist_regist)).check(matches(isEnabled())).perform(click());
        onView(withId(R.id.tv_error_tip)).check(matches(withText(mActivityRule.getActivity().getString(R.string.username_toast_not_symbol_hint))));
    }

    /************************************** 手机号 ************************************/


    /**
     * summary                       是否可输入不合法的手机号码
     * <p>
     * steps                             ① 输入用户名“啊哈”
     * ② 手机号：12345685
     * ③ 发送验证码;
     * <p>
     * expected                       发送验证码按钮不亮
     *
     * @throws Exception
     */
    @Test
    public void errorPhoneNumber_length() throws Exception {
        onView(withId(R.id.et_regist_phone)).perform(typeText("12345685"));
        onView(withId(R.id.et_regist_username)).perform(replaceText(TEST_USER_NAME));
        onView(withId(R.id.bt_regist_send_vertify_code)).check(matches(disEnabled()));
    }

    /**
     * summary                       是否可输入不合法的手机号码
     * <p>
     * steps                             ① 输入用户名“啊哈”
     * ② 手机号：
     * ③ 发送验证码
     * <p>
     * expected                     提示请输入正确的手机号
     *
     * @throws Exception
     */
    @Test
    public void errorPhoneNumber_symbol() throws Exception {
        onView(withId(R.id.et_regist_username)).perform(replaceText(TEST_USER_NAME));
        onView(withId(R.id.et_regist_phone)).perform(typeText("1361802982#"));
        onView(withId(R.id.bt_regist_send_vertify_code)).check(matches(isEnabled())).perform(click());
        onView(withId(R.id.tv_error_tip)).check(matches(withText(mActivityRule.getActivity().getString(R.string.phone_number_toast_hint))));
    }

    /**
     * summary                      手机号是否可以为空
     * <p>
     * steps                             手机号不填
     * <p>
     * expected                     发验证码按钮不亮，无法点击
     *
     * @throws Exception
     */
    @Test
    public void errorPhoneNumber_null() throws Exception {
        onView(withId(R.id.et_regist_username)).perform(replaceText(TEST_USER_NAME));
        onView(withId(R.id.et_regist_phone)).perform(typeText(""));
        onView(withId(R.id.bt_regist_send_vertify_code)).check(matches(disEnabled()));
    }

    /**
     * summary                      手机号是否可输入12位数字
     * <p>
     * steps                             手机号输入数字
     * <p>
     * expected                     手机要只能11位，当输入11位就不能输入了
     *
     * @throws Exception
     */
    @Test
    public void errorPhoneNumber_maxLength() throws Exception {
        onView(withId(R.id.et_regist_phone)).perform(typeText("156940050091"));
        onView(withId(R.id.et_regist_phone)).check(matches(withText("15694005009")));
    }
    /**
     * summary                      输入合法手机号
     * <p>
     * steps                             手机号输入数字
     * <p>
     * expected                     手机要只能11位，当输入11位就不能输入了
     *
     * @throws Exception
     */
    @Test
    public void correctPhoneNumber(){
        onView(withId(R.id.et_regist_phone)).perform(typeText(USER_PHONE));
        onView(withId(R.id.bt_regist_send_vertify_code)).check(matches(isEnabled())).perform(click());
        onView(withId(R.id.tv_error_tip)).check(matches(isDisappear()));
    }
}