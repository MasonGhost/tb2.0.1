<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.functions.Action1;

import static com.zhiyicx.thinksnsplus.modules.register.RegisterActivityTest.USER_PHONE;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/13
 * @Contact master.jungle68@gmail.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FindPasswordPresenterTest {
    @Rule
    public ActivityTestRule<FindPasswordActivity> mActivityRule = new ActivityTestRule(FindPasswordActivity.class);

    private PasswordClient mPasswordClient;
    private CommonClient mCommonClient;

    @Before
    public void initActivity() {
        RxUnitTestTools.openRxTools();
        mPasswordClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getPasswordClient();
        mCommonClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getCommonClient();

    }

    /**
     * summary    因为某些原因导致找回失败
     * steps        1.输入正确手机号、密码  2.输入错误的验证码 3.点确认按钮
     * expected   errorTip显示登录失败的原因
     */
    @Test
    public void findPasswordFailure() throws Exception {
        mPasswordClient.findPassword("failure", USER_PHONE, "12344", "dsafdsa")
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertFalse(true);
                        } else {
                            // 登录失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable, "error");
                        assertFalse(false);
                    }
                });

    }

    /**
     * 测试修改密码网络请求成功
     *
     * @throws Exception
     */
    @Test
    public void findPasswordSuccess() throws Exception {
        mPasswordClient.findPassword("success", USER_PHONE, "1244", "dsafdsa")
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertTrue(true);
                        } else {
                            // 登录失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable, "error");
                        assertFalse(false);
                    }
                });

    }

    /**
     * summary    因为某些原因导致修改失败
     * steps          1.输入错误的手机号 3.点发送按钮
     * expected   errorTip显示登录失败的原因
     */
    @Test
    public void getVertifyCodeFailure() throws Exception {
        mCommonClient.getVertifyCode("failure", USER_PHONE, "change")
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertFalse(true);
                        } else {
                            // 登录失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable, "error");
                        assertFalse(false);
                    }
                });

    }

    /**
     * 测试修改密码网络请求成功
     *
     * @throws Exception
     */
    @Test
    public void getVertifyCodeSuccess() throws Exception {
        mCommonClient.getVertifyCode("success", USER_PHONE, "change")
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertTrue(true);
                        } else {
                            // 登录失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable, "error");
                        assertFalse(false);
                    }
                });

    }
=======
package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.functions.Action1;

import static com.zhiyicx.thinksnsplus.modules.register.RegisterActivityTest.USER_PHONE;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/13
 * @Contact master.jungle68@gmail.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FindPasswordPresenterTest {
    @Rule
    public ActivityTestRule<FindPasswordActivity> mActivityRule = new ActivityTestRule(FindPasswordActivity.class);

    private PasswordClient mPasswordClient;
    private CommonClient mCommonClient;

    @Before
    public void initActivity() {
        RxUnitTestTools.openRxTools();
        mPasswordClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getPasswordClient();
        mCommonClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getCommonClient();

    }

    /**
     * summary    因为某些原因导致找回失败
     * steps        1.输入正确手机号、密码  2.输入错误的验证码 3.点确认按钮
     * expected   errorTip显示登录失败的原因
     */
    @Test
    public void findPasswordFailure() throws Exception {
        mPasswordClient.findPassword("failure", USER_PHONE, "12344", "dsafdsa")
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertFalse(true);
                        } else {
                            // 登录失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable, "error");
                        assertFalse(false);
                    }
                });

    }

    /**
     * 测试修改密码网络请求成功
     *
     * @throws Exception
     */
    @Test
    public void findPasswordSuccess() throws Exception {
        mPasswordClient.findPassword("success", USER_PHONE, "1244", "dsafdsa")
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertTrue(true);
                        } else {
                            // 登录失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable, "error");
                        assertFalse(false);
                    }
                });

    }

    /**
     * summary    因为某些原因导致修改失败
     * steps          1.输入错误的手机号 3.点发送按钮
     * expected   errorTip显示登录失败的原因
     */
    @Test
    public void getVertifyCodeFailure() throws Exception {
        mCommonClient.getVertifyCode("failure", USER_PHONE, "change")
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertFalse(true);
                        } else {
                            // 登录失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable, "error");
                        assertFalse(false);
                    }
                });

    }

    /**
     * 测试修改密码网络请求成功
     *
     * @throws Exception
     */
    @Test
    public void getVertifyCodeSuccess() throws Exception {
        mCommonClient.getVertifyCode("success", USER_PHONE, "change")
                .subscribe(new Action1<BaseJson<CacheBean>>() {
                    @Override
                    public void call(BaseJson<CacheBean> integerBaseJson) {
                        LogUtils.d(integerBaseJson.toString());
                        if (integerBaseJson.isStatus()) {
                            // 成功跳转:当前不可能发生
                            assertTrue(true);
                        } else {
                            // 登录失败
                            assertFalse(false);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable, "error");
                        assertFalse(false);
                    }
                });

    }
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
}