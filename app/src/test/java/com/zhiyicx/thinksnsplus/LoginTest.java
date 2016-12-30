package com.zhiyicx.thinksnsplus;

import android.widget.Button;
import android.widget.EditText;

import com.zhiyicx.common.dagger.module.AppModule;
import com.zhiyicx.common.dagger.module.HttpClientModule;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.dagger.module.ShareModule;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.CacheModule;
import com.zhiyicx.thinksnsplus.base.DaggerAppComponent;
import com.zhiyicx.thinksnsplus.base.ServiceModule;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
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
@Config(constants = BuildConfig.class, sdk = 21,application = TestApplicaiton.class)
public class LoginTest {
        private EditText phoneEt;// 手机号输入框
    private EditText passEt;// 密码输入框
    private Button loginBtn;// 登录按钮
    private LoginActivity mLoginActivity;

    @Before
    public void initLoginActivity() {
//        TestApplicaiton app = (TestApplicaiton) RuntimeEnvironment.application;
        // Setting up the mock module
//        MockHttpClientModule mockHttpClientModule = new MockHttpClientModule();
//                ( HttpClientModule// 用于提供 okhttp 和 retrofit 的单列
//                .buidler()
//                .baseurl(ApiConfig.APP_DOMAIN)
//                .globeHttpHandler(mock(RequestInterceptListener.class))
//                .responseErroListener(mock(ResponseErroListener.class))
//                );
//        ServiceModule serviceModule=new ServiceModule();
//        app.setServiceModule(serviceModule);
//        CacheModule cacheModule=new CacheModule();
//        app.setCacheModule(cacheModule);
//        ImageModule imageModule=new ImageModule(mock(GlideImageLoaderStrategy.class));
//        app.setImageModule(imageModule);
//        ShareModule shareModule=new ShareModule(mock(UmengSharePolicyImpl.class));
//        app.setShareModule(shareModule);
        AppComponent appComponent = DaggerAppComponent
                .builder()
                .appModule( Mockito.mock(AppModule.class))// baseApplication 提供
                .httpClientModule( Mockito.mock(HttpClientModule.class))// baseApplication 提供
                .imageModule( Mockito.mock(ImageModule.class))// // 图片加载框架
                .shareModule( Mockito.mock(ShareModule.class))// 分享框架
                .serviceModule( Mockito.mock(ServiceModule.class))// 需自行创建
                .cacheModule( Mockito.mock(CacheModule.class))// 需自行创建
                .build();
        AppApplication.AppComponentHolder.setAppComponent(appComponent);
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
