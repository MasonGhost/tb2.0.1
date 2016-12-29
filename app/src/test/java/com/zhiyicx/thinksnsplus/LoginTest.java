package com.zhiyicx.thinksnsplus;

import android.widget.Button;
import android.widget.EditText;

import com.zhiyicx.baseproject.impl.imageloader.GlideImageLoaderStrategy;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.dagger.module.AppModule;
import com.zhiyicx.common.dagger.module.HttpClientModule;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.dagger.module.ShareModule;
import com.zhiyicx.thinksnsplus.base.CacheModule;
import com.zhiyicx.thinksnsplus.base.ServiceModule;
import com.zhiyicx.thinksnsplus.dagger.MockHttpClientModule;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/28
 * @contact email:450127106@qq.com
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21,application = TestApplicaiton.class)
public class LoginTest {
        private EditText phoneEt;// 手机号输入框
    private EditText passEt;// 密码输入框
    private Button loginBtn;// 登录按钮
    private LoginActivity mLoginActivity;

    @Before
    public void initLoginActivity() {
        TestApplicaiton app = (TestApplicaiton) RuntimeEnvironment.application;
        AppModule appModule=new AppModule(app);
        app.setAppModule(appModule);
        // Setting up the mock module
        MockHttpClientModule mockHttpClientModule = new MockHttpClientModule(HttpClientModule.buidler());
        app.setHttpClientModule(mockHttpClientModule);
        ServiceModule serviceModule=new ServiceModule();
        app.setServiceModule(serviceModule);
        CacheModule cacheModule=new CacheModule();
        app.setCacheModule(cacheModule);
        ImageModule imageModule=new ImageModule(mock(GlideImageLoaderStrategy.class));
        app.setImageModule(imageModule);
        ShareModule shareModule=new ShareModule(mock(UmengSharePolicyImpl.class));
        app.setShareModule(shareModule);
        app.initComponent();
        mLoginActivity = Robolectric.setupActivity(LoginActivity.class);
        phoneEt = (EditText) mLoginActivity.findViewById(R.id.et_login_phone);
        passEt = (EditText) mLoginActivity.findViewById(R.id.et_login_password);
        loginBtn = (Button) mLoginActivity.findViewById(R.id.bt_login_login);
    }

    /**
     * 不输入手机号，登录按钮无法点击
     */
    @Test
    public void clickableWhenNoPhone() {
        phoneEt.setText("");
        passEt.setText("hahhaha");
        assertFalse(loginBtn.isClickable());
//        assertFalse(false);
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
