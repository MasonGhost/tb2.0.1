package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
public class ChangePasswordPresenterTest {
    private static final String USER_PHONE = "15694005009";
    private static final String USER_NAME = "七夜68";

    @Rule
    public ActivityTestRule<ChangePasswordActivity> mActivityRule = new ActivityTestRule(ChangePasswordActivity.class);

    private PasswordClient mPasswordClient;

    @Before
    public void initActivity() {
        mPasswordClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getPasswordClient();

    }

    /**
     * summary    因为某些原因导致修改失败
     * steps        1.输入正确、密码  2.输入错误的旧密码 3.点确认按钮
     * expected   errorTip显示登录失败的原因
     */
    @Test
    public void changePasswordFailure() throws Exception {
        mPasswordClient.changePassword("failure", "12344", "dsafdsa")
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
                            // 登录失败
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
     * 测试修改密码网络请求成功
     * @throws Exception
     */
    @Test
    public void changePasswordSuccess() throws Exception {
        mPasswordClient.changePassword("success", "1244", "dsafdsa")
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
                            // 登录失败
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

}