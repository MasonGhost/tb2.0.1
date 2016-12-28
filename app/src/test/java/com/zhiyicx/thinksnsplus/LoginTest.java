package com.zhiyicx.thinksnsplus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/28
 * @contact email:450127106@qq.com
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LoginTest {
    /*    private EditText phoneEt;// 手机号输入框
    private EditText passEt;// 密码输入框
    private Button loginBtn;// 登录按钮
    private LoginActivity mLoginActivity;*/

    @Before
    public void initLoginActivity() {
       /* mLoginActivity = Robolectric.setupActivity(LoginActivity.class);
        phoneEt = (EditText) mLoginActivity.findViewById(R.id.et_login_phone);
        passEt = (EditText) mLoginActivity.findViewById(R.id.et_login_password);
        loginBtn = (Button) mLoginActivity.findViewById(R.id.bt_login_login);*/
    }

    /**
     * 不输入手机号，登录按钮无法点击
     */
    @Test
    public void clickableWhenNoPhone() {
        /*phoneEt.setText("");
        passEt.setText("hahhaha");
        assertFalse(loginBtn.isClickable());*/
        assertFalse(false);
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
