package com.zhiyicx.thinksnsplus.modules.register;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
import static com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter.SNS_TIME;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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

    private RegisterClient mRegisterClient;

    @Before
    public void initActivity() {
        mRegisterClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getRegisterClient();

    }

    /**
     * summary    因为某些原因导致注册失败，比如验证码错误
     * steps        1.输入正确的手机号、用户名、密码  2.输入错误的验证码码 3.点击注册按钮
     * expected   errorTip显示登陆失败的原因
     */
    @Test
    public void registerFailure() throws Exception {
        mRegisterClient.register("failure", USER_PHONE, USER_NAME, "12344", "dsafdsa")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertFalse(true);
                        } else {
                            // 登陆失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable,"error");
                        assertFalse(false);
                    }
                });

    }

    /**
     * 测试注册网络请求成功
     * @throws Exception
     */
    @Test
    public void registerSuccess() throws Exception {
        mRegisterClient.register("success", USER_PHONE, USER_NAME, "1244", "dsafdsa")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertTrue(true);
                        } else {
                            // 登陆失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable,"error");
                        assertFalse(false);
                    }
                });

    }
    /*******************************************  用户名  *********************************************/

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
     * <p>
     * steps                        1.输入用户名“测￥%”
     * 2.填写手机号，验证码，密码
     * 3.点击注册
     * <p>
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

    /*******************************************  手机号  *********************************************/


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
    public void correctPhoneNumber() {
        onView(withId(R.id.et_regist_phone)).perform(typeText(USER_PHONE));
        onView(withId(R.id.bt_regist_send_vertify_code)).check(matches(isEnabled())).perform(click());
        onView(withId(R.id.tv_error_tip)).check(matches(isDisappear()));
    }

    /*******************************************  验证码  *********************************************/

    /**
     * summary                      不输入验证码
     * <p>
     * steps                        1.输入手机号获取验证码
     * 2.不填写验证码,
     * <p>
     * <p>
     * expected                    其他都填写情况下，注册按钮颜色不亮无法点击
     *
     * @throws Exception
     */
    @Test
    public void vertifyCode_null() {
        onView(withId(R.id.et_regist_phone)).perform(typeText(USER_PHONE));
        onView(withId(R.id.et_regist_vertify_code)).perform(typeText(""));
        onView(withId(R.id.et_regist_password)).perform(typeText("fdagiasdg"));
        onView(withId(R.id.et_regist_username)).perform(replaceText(USER_NAME));
        onView(withId(R.id.bt_regist_regist)).check(matches(disEnabled()));

    }

    /**
     * summary                      验证码倒计时时是否还能点击
     * <p>
     * steps                        1.输入手机号获取验证码
     * 2.点击正在倒计时的验证码
     * <p>
     * expected                    点击无效，倒计时结束后才能继续点击发送
     *
     * @throws Exception
     */
    @Test
    public void vertifyCode_countDownTimer() throws Exception {
        onView(withId(R.id.et_regist_phone)).perform(typeText(USER_PHONE));
        onView(withId(R.id.bt_regist_send_vertify_code)).perform(click());
        onView(withId(R.id.bt_regist_send_vertify_code)).check(matches(disEnabled()));
        Thread.sleep(60 * 1000);
        onView(withId(R.id.bt_regist_send_vertify_code)).check(matches(isEnabled()));
    }

    /**
     * summary                      验证码倒计时时切换手机号是否能点击重新发送验证码
     * <p>
     * steps                        1.输入手机号获取验证码
     * 2.删除手机号重新输入新的
     * <p>
     * expected                    倒计时依旧进行，待倒计时结束才可重新点击获取验证码
     *
     * @throws Exception
     */
    @Test
    public void vertifyCode_change() throws Exception {
        onView(withId(R.id.et_regist_phone)).perform(typeText(USER_PHONE));
        onView(withId(R.id.bt_regist_send_vertify_code)).perform(click());
        onView(withId(R.id.et_regist_phone)).perform(typeText("15694005008"));
        onView(withId(R.id.bt_regist_send_vertify_code)).check(matches(disEnabled()));
        Thread.sleep(SNS_TIME);
        onView(withId(R.id.bt_regist_send_vertify_code)).check(matches(isEnabled()));
    }


    /*******************************************  密码  *********************************************/

    /**
     * summary                      不输入密码
     * <p>
     * steps                         1.输入昵称“测试124”
     * 2.填写手机号、验证码
     * 3.不输入密码
     * 4.点击注册
     * <p>
     * expected                    注册按钮颜色不亮，无法点击
     *
     * @throws Exception
     */
    @Test
    public void password_null() throws Exception {
        onView(withId(R.id.et_regist_username)).perform(replaceText(USER_NAME));
        onView(withId(R.id.et_regist_phone)).perform(typeText(USER_PHONE));
        onView(withId(R.id.et_regist_vertify_code)).perform(typeText("2124"));
        onView(withId(R.id.et_regist_password)).perform(typeText(""));
        onView(withId(R.id.bt_regist_regist)).check(matches(disEnabled()));
    }

    /**
     * summary                      输入5位密码
     * <p>
     * steps                         1.输入昵称“测试124”
     * 2.填写手机号、验证码
     * 3.不输入密码
     * 4.点击注册
     * <p>
     * expected                    注册按钮颜色不亮，无法点击
     *
     * @throws Exception
     */
    @Test
    public void password_erroLength() throws Exception {
        onView(withId(R.id.et_regist_username)).perform(replaceText(USER_NAME));
        onView(withId(R.id.et_regist_phone)).perform(typeText(USER_PHONE));
        onView(withId(R.id.et_regist_vertify_code)).perform(typeText("2124"));
        onView(withId(R.id.et_regist_password)).perform(replaceText("12345"));
        onView(withId(R.id.bt_regist_regist)).check(matches(isEnabled())).perform(click());
        onView(withId(R.id.tv_error_tip)).check(matches(withText(mActivityRule.getActivity().getString(R.string.password_toast_hint))));
    }

    /**
     * summary                      输入6位密码
     * <p>
     * steps                         1.输入昵称“测试124”
     * 2.填写手机号、验证码
     * 3.不输入密码
     * 4.点击注册
     * <p>
     * expected                   注册成功跳转首页
     *
     * @throws Exception
     */
    @Test
    public void password_correct() throws Exception {
        onView(withId(R.id.et_regist_username)).perform(replaceText(USER_NAME));
        onView(withId(R.id.et_regist_phone)).perform(typeText(USER_PHONE));
        onView(withId(R.id.et_regist_vertify_code)).perform(typeText("2124"));
        onView(withId(R.id.et_regist_password)).perform(replaceText("123456"));
        onView(withId(R.id.bt_regist_regist)).check(matches(isEnabled())).perform(click());
        onView(withId(R.id.tv_error_tip)).check(matches(isDisappear()));
    }

    /*******************************************  手机号正则 单元测试  *********************************************/

    /**
     * summary                       判断手机号必须为 11 位
     * <p>
     * steps                         1.输入 1234; 2.输入 18908199568
     * <p>
     * expected                      1.false 2.true
     *
     * @throws Exception
     */
    @Test
    public void phoneNumber_length() throws Exception {
        String phone="1234";
        assertFalse(RegexUtils.isMobileExact(phone));
        phone="18908199568";
        assertTrue(RegexUtils.isMobileExact(phone));
    }

    /**
     * summary                       判断手机号第一位必须为 1
     * <p>
     * steps                         1.输入 28908199568; 2.输入 18908199568
     * <p>
     * expected                      1.false 2.true
     *
     * @throws Exception
     */
    @Test
    public void phoneNumber_startWith1() throws Exception {
        String phone="28908199568";
        assertFalse(RegexUtils.isMobileExact(phone));
        phone="18908199568";
        assertTrue(RegexUtils.isMobileExact(phone));
    }

    /**
     * summary                       判断手机号第二位必须为 34578 其中之一
     * <p>
     * steps                         1.输入 11908199568; 2.输入 18908199568
     * <p>
     * expected                      1.false 2.true
     *
     * @throws Exception
     */
    @Test
    public void phoneNumber_secondNum() throws Exception {
        String phone="11908199568";
        assertFalse(RegexUtils.isMobileExact(phone));
        phone="18908199568";
        assertTrue(RegexUtils.isMobileExact(phone));
    }

    /*******************************************  密码正则 单元测试  *********************************************/

    /**
     * summary                       判断密码大于 5 位
     * <p>
     * steps                         1.输入 Test1; 2.输入 Test12
     * <p>
     * expected                      1.false 2.true
     *
     * @throws Exception
     */
    @Test
    public void password_length() throws Exception {
        password_erroLength();
        password_correct();
    }

}