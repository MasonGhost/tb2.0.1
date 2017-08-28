package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.remote.PasswordClient;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;

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


    @Rule
    public ActivityTestRule<ChangePasswordActivity> mActivityRule = new ActivityTestRule(ChangePasswordActivity.class);

    private PasswordClient mPasswordClient;

    @Before
    public void initActivity() {
        RxUnitTestTools.openRxTools();
        mPasswordClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getPasswordClient();

    }

    /**
     * summary    因为某些原因导致修改失败
     * steps        1.输入正确、密码  2.输入错误的旧密码 3.点确认按钮
     * expected   errorTip显示登录失败的原因
     */
    @Test
    public void changePasswordFailure() throws Exception {

    }

    /**
     * 测试修改密码网络请求成功
     * @throws Exception
     */
    @Test
    public void changePasswordSuccess() throws Exception {


    }

}